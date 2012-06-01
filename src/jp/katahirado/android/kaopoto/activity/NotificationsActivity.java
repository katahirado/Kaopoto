package jp.katahirado.android.kaopoto.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.JsonManager;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.NotificationsAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsActivity extends ListActivity {
    private JSONArray notificationArray;
    private Intent intent;
    private ProgressDialog dialog;
    private ProfilesDao profilesDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        intent = getIntent();
        Bundle extras = intent.getExtras();
        JSONArray profileArray;
        try {
            JSONArray array = new JSONObject(extras.getString(Const.API_RESPONSE)).getJSONArray(Const.DATA);
            JSONObject q1 = array.getJSONObject(0);
            JSONObject q2 = array.getJSONObject(1);
            notificationArray = q1.getJSONArray(Const.FQL_RESULT_SET);
            profileArray = q2.getJSONArray(Const.FQL_RESULT_SET);
        } catch (JSONException e) {
            e.printStackTrace();
            notificationArray = new JSONArray();
            profileArray = new JSONArray();
        }
        JsonManager.mJsonArray = profileArray;
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                JsonManager.setAList(Const.PIC_SQUARE);
                profilesDao.bulkInsert();
            }
        })).start();
        setListAdapter(new NotificationsAdapter(this, notificationArray));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String objectId = "";
        String objectType = "";
        String fUrl = "";
        try {
            JSONObject object = notificationArray.getJSONObject(position);
            objectId = object.getString(Const.OBJECT_ID);
            objectType = object.getString("object_type");
            fUrl = KaopotoUtil.getMobileURL(object.getString("href"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (objectType.equals(Const.STREAM)) {
            goToPostItem(objectId, PostItemActivity.class);
        } else if (objectType.equals(Const.EVENT)) {
            goToPostItem(objectId, EventItemActivity.class);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fUrl));
            startActivity(intent);
        }
    }

    private void goToPostItem(String objectId, final Class<?> cls) {
        dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
        Bundle params = new Bundle();
        params.putString("date_format", "U");
        Utility.mAsyncRunner.request(objectId, params, new BaseRequestListener() {
            @Override
            public void onComplete(final String response, final Object state) {
                dialog.dismiss();
                intent = new Intent(getApplicationContext(), cls);
                intent.putExtra(Const.API_RESPONSE, response);
                startActivity(intent);
            }
        });
    }
}