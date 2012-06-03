package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.facebook.android.*;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.ProfileData;
import org.json.JSONException;
import org.json.JSONObject;


public class KaopotoActivity extends Activity implements AdapterView.OnItemClickListener {
    final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
    private final static int UPDATE_STATUS = 0;
    private final static int GET_FRIENDS = 1;
    private final static int GET_PAGES = 2;
    private final static int NOTIFICATIONS = 3;
    private TextView text;
    private ImageView userPicture;
    private Handler handler;
    private String[] permissions = {"publish_stream", "read_stream", "user_likes",
            "manage_notifications", "friends_birthday"};
    private String[] main_items = {"Update Status", "Friends", "Pages", "Notifications"};
    private ProgressDialog dialog;
    private Bundle params;
    private Intent intent;
    private ProfilesDao profilesDao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.katahirado.android.kaopoto.R.layout.main);

        handler = new Handler();
        Utility.mFacebook = new Facebook(Const.APP_ID);
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        LoginButton login = (LoginButton) findViewById(jp.katahirado.android.kaopoto.R.id.login);
        text = (TextView) findViewById(jp.katahirado.android.kaopoto.R.id.txt);
        userPicture = (ImageView) findViewById(jp.katahirado.android.kaopoto.R.id.user_pic);

        SessionStore.restore(Utility.mFacebook, this);
        SessionEvents.addAuthListener(new SessionEvents.AuthListener() {
            @Override
            public void onAuthSucceed() {
                requestUserData();
            }

            @Override
            public void onAuthFail(String error) {
                text.setText("ログイン失敗: " + error);
            }
        });

        SessionEvents.addLogoutListener(new SessionEvents.LogoutListener() {
            @Override
            public void onLogoutBegin() {
                text.setText("ログアウト中...");
            }

            @Override
            public void onLogoutFinish() {
                text.setText("ログアウトしました");
                userPicture.setImageBitmap(null);
            }
        });

        login.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);

        if (Utility.mFacebook.isSessionValid()) {
            requestUserData();
        }

        ListView mainList = (ListView) findViewById(jp.katahirado.android.kaopoto.R.id.main_list);

        mainList.setOnItemClickListener(this);
        mainList.setAdapter(new ArrayAdapter<String>(this,
                jp.katahirado.android.kaopoto.R.layout.main_list_row, main_items));

        String message = getIntent().getStringExtra(Const.MESSAGE);
        if (message != null) {
            if (Utility.mFacebook.isSessionValid()) {
                goToWallPost(message);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AUTHORIZE_ACTIVITY_RESULT_CODE:
                Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.mFacebook != null) {
            if (!Utility.mFacebook.isSessionValid()) {
                text.setText("ログアウトしています ");
                userPicture.setImageBitmap(null);
            } else {
                Utility.mFacebook.extendAccessTokenIfNeeded(this, null);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case UPDATE_STATUS:
                goToWallPost(null);
                break;
            case GET_FRIENDS:
                if (!Utility.mFacebook.isSessionValid()) {
                    Util.showAlert(this, getString(jp.katahirado.android.kaopoto.R.string.warning),
                            getString(jp.katahirado.android.kaopoto.R.string.firstLogin));
                } else {
                    dialog = ProgressDialog.show(this, "",
                            getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                    params = new Bundle();
                    params.putString(Const.FIELDS, Const.NAME + "," + Const.PICTURE + "," + Const.BIRTHDAY);
                    Utility.mAsyncRunner.request("me/friends", params, new BaseRequestListener() {
                        @Override
                        public void onComplete(final String response, final Object state) {
                            dialog.dismiss();
                            intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                            intent.putExtra(Const.API_RESPONSE, response);
                            startActivity(intent);
                        }
                    });
                }
                break;
            case GET_PAGES:
                if (!Utility.mFacebook.isSessionValid()) {
                    Util.showAlert(this, getString(jp.katahirado.android.kaopoto.R.string.warning),
                            getString(jp.katahirado.android.kaopoto.R.string.firstLogin));
                } else {
                    dialog = ProgressDialog.show(this, "",
                            getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                    params = new Bundle();
                    params.putString(Const.FIELDS, Const.NAME + "," + Const.PICTURE + "," + Const.CATEGORY);
                    Utility.mAsyncRunner.request("me/likes", params, new BaseRequestListener() {
                        @Override
                        public void onComplete(final String response, final Object state) {
                            dialog.dismiss();
                            intent = new Intent(getApplicationContext(), PagesListActivity.class);
                            intent.putExtra(Const.API_RESPONSE, response);
                            startActivity(intent);
                        }
                    });
                }
                break;
            case NOTIFICATIONS:
                if (!Utility.mFacebook.isSessionValid()) {
                    Util.showAlert(this, getString(jp.katahirado.android.kaopoto.R.string.warning),
                            getString(jp.katahirado.android.kaopoto.R.string.firstLogin));
                } else {
                    dialog = ProgressDialog.show(this, "",
                            getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                    String query1 =
                            "\"q1\":\"SELECT notification_id, recipient_id,sender_id,created_time," +
                                    "updated_time,title_html,title_text, body_html,body_text, href,app_id," +
                                    "is_unread,is_hidden,object_id,object_type,icon_url FROM" +
                                    " notification WHERE recipient_id=me()\"";
                    String query2 = "\"q2\":\"select id,pic_square from profile" +
                            " where id in (select sender_id from #q1)\"";
                    params = new Bundle();
                    params.putString("q", "{" + query1 + "," + query2 + "}");
                    Utility.mAsyncRunner.request("fql", params, new BaseRequestListener() {
                        @Override
                        public void onComplete(final String response, final Object state) {
                            dialog.dismiss();
                            intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                            intent.putExtra(Const.API_RESPONSE, response);
                            startActivity(intent);
                        }
                    });

                }
                break;
        }
    }

    private void goToWallPost(String message) {
        intent = new Intent(this, WallPostActivity.class);
        if (message != null) {
            intent.putExtra(Const.MESSAGE, message);
        }
        startActivity(intent);
    }

    private void requestUserData() {
        text.setText(getString(jp.katahirado.android.kaopoto.R.string.loading));
        params = new Bundle();
        params.putString(Const.FIELDS, Const.NAME + "," + Const.PICTURE);
        Utility.mAsyncRunner.request("me", params, new BaseRequestListener() {
            @Override
            public void onComplete(final String response, final Object state) {
                final ProfileData currentUser;
                try {
                    currentUser = new ProfileData(new JSONObject(response), Const.PICTURE);
                    profilesDao = new ProfilesDao(new DBOpenHelper(getApplicationContext()).getWritableDatabase());
                    (new Thread(new Runnable() {
                        @Override
                        public void run() {
                            profilesDao.insert(currentUser);
                        }
                    })).start();
                    Utility.userUID = currentUser.getUid();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(currentUser.getName());
                            userPicture.setImageBitmap(Utility.getBitmap(currentUser.getPicture()));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
