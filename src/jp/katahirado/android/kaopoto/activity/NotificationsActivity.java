package jp.katahirado.android.kaopoto.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.*;
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
    private ProgressDialog dialog;
    private Class<?> cls;
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