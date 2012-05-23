package jp.katahirado.android.kaopoto;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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
//        KaopotoDBHelper dbHelper = new KaopotoDBHelper(this);
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        SQLiteManager.setProfileData(profileArray, database);
        setListAdapter(new NotificationsAdapter(this, notificationArray));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        String objectId = "";
//        int typeId = 0;
//        try {
//            JSONObject object = notificationArray.getJSONObject(position);
//            objectId = object.getString(Const.OBJECT_ID);
//            typeId = KaopotoUtil.stringToTypeID(object.getString("object_type"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        switch (typeId) {
//            case Const.STREAM_ID:
//                intent = new Intent(this, PostItemActivity.class);
//                intent.putExtra(Const.OBJECT_ID, objectId);
//                startActivity(intent);
//                break;
//            case Const.EVENT_ID:
//                break;
//        }
        String fUrl = "";
        try {
            String url = notificationArray.getJSONObject(position).getString("href");
            fUrl = url.replace("http://www.facebook.com/", Const.FACEBOOK_MOBILE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fUrl));
        startActivity(intent);
    }
}