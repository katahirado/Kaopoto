package jp.katahirado.android.kaopoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.facebook.android.*;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.katahirado.android.kaopoto.R.layout.main);

        handler = new Handler();
        Utility.mFacebook = new Facebook(Const.APP_ID);
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        LoginButton login = (LoginButton) findViewById(R.id.login);
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

        ListView mainList = (ListView) findViewById(R.id.main_list);

        mainList.setOnItemClickListener(this);
        mainList.setAdapter(new ArrayAdapter<String>(this, jp.katahirado.android.kaopoto.R.layout.main_list_item, main_items));
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
                params = new Bundle();
                params.putString("caption", getString(jp.katahirado.android.kaopoto.R.string.app_name));
                Utility.mFacebook.dialog(this, "feed", params, new BaseDialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        final String postId = values.getString("post_id");
                        if (postId != null) {
                            Toast.makeText(getApplicationContext(), "投稿しました", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
                break;
            case GET_FRIENDS:
                if (!Utility.mFacebook.isSessionValid()) {
                    Util.showAlert(this,getString(R.string.warning), getString(R.string.firstLogin));
                } else {
                    dialog = ProgressDialog.show(this, "",getString(R.string.loading), true, true);
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
                    Util.showAlert(this, getString(R.string.warning), getString(R.string.firstLogin));
                } else {
                    dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
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
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/notifications"));
                startActivity(intent);
//                if (!Utility.mFacebook.isSessionValid()) {
//                    Util.showAlert(this, getString(R.string.warning), getString(R.string.firstLogin));
//                } else {
//                    dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
//                    params = new Bundle();
//                    params.putString("include_read", "1");
//                    Utility.mAsyncRunner.request("me/notifications", params, new BaseRequestListener() {
//                        @Override
//                        public void onComplete(final String response, final Object state) {
//                            dialog.dismiss();
//                            intent = new Intent(getApplicationContext(), NotificationsActivity.class);
//                            intent.putExtra(Const.API_RESPONSE, response);
//                            startActivity(intent);
//                        }
//                    });
//
//                }
                break;
        }
    }

    private void requestUserData() {
        text.setText(getString(R.string.loading));
        params = new Bundle();
        params.putString(Const.FIELDS, Const.NAME + "," + Const.PICTURE);
        Utility.mAsyncRunner.request("me", params, new BaseRequestListener() {
            @Override
            public void onComplete(final String response, final Object state) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);

                    final String picURL = jsonObject.getString(Const.PICTURE);
                    final String name = jsonObject.getString(Const.NAME);
                    Utility.userUID = jsonObject.getString(Const.ID);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(name);
                            userPicture.setImageBitmap(Utility.getBitmap(picURL));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
