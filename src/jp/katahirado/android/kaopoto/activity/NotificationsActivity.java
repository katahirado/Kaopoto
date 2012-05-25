package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.*;
import jp.katahirado.android.kaopoto.activity.PostItemActivity;
import jp.katahirado.android.kaopoto.adapter.NotificationsAdapter;
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
    private JSONArray profileArray;
    private ProgressDialog dialog;
    private DBOpenHelper dbHelper;
    private Class<?> cls;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        intent = getIntent();
        Bundle extras = intent.getExtras();
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
        dbHelper = new DBOpenHelper(this);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                JsonManager.setAList(Const.PIC_SQUARE);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                SQLiteManager.setProfileData(database);
                database.close();
            }
        })).start();
        setListAdapter(new NotificationsAdapter(this, notificationArray));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String objectId = "";
        int typeId = 0;
        try {
            JSONObject object = notificationArray.getJSONObject(position);
            objectId = object.getString(Const.OBJECT_ID);
            typeId = KaopotoUtil.stringToTypeID(object.getString("object_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (typeId) {
            case Const.STREAM_ID:
                cls = PostItemActivity.class;
                break;
            case Const.EVENT_ID:
                cls = EventItemActivity.class;
                break;
        }
        dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
        Bundle params = new Bundle();
        params.putString("date_format","U");
        Utility.mAsyncRunner.request(objectId,params,new BaseRequestListener() {
            @Override
            public void onComplete(final String response, final Object state) {
                dialog.dismiss();
                intent = new Intent(getApplicationContext(), cls);
                intent.putExtra(Const.API_RESPONSE, response);
                startActivity(intent);
            }
        });
//        String fUrl = "";
//        try {
//            String url = notificationArray.getJSONObject(position).getString("href");
//            fUrl = url.replace("http://www.facebook.com/", Const.FACEBOOK_MOBILE_URL);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fUrl));
//        startActivity(intent);
    }
}