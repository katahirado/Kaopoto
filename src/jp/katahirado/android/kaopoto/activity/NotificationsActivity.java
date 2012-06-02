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
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.NotificationsAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.NotificationData;
import jp.katahirado.android.kaopoto.model.ProfileData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsActivity extends ListActivity {
    private Intent intent;
    private ProgressDialog dialog;
    private ProfilesDao profilesDao;
    private ArrayList<ProfileData> profileList;
    private NotificationsAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        ArrayList<NotificationData> notificationList;
        try {
            JSONArray array = new JSONObject(getIntent().getStringExtra(Const.API_RESPONSE)).getJSONArray(Const.DATA);
            notificationList = parseNotifications(array.getJSONObject(0));
            profileList = parseProfileList(array.getJSONObject(1));
        } catch (JSONException e) {
            e.printStackTrace();
            notificationList = new ArrayList<NotificationData>();
            profileList = new ArrayList<ProfileData>();
        }
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                profilesDao.bulkInsert(profileList);
            }
        })).start();
        adapter = new NotificationsAdapter(this, notificationList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        NotificationData notificationData = adapter.getItem(position);
        String objectId = notificationData.getObjectId();
        String objectType = notificationData.getObjectType();
        String url = notificationData.getHref();
        if (objectType.equals(Const.STREAM)) {
            goToPostItem(objectId, PostItemActivity.class);
        } else if (objectType.equals(Const.EVENT)) {
            goToPostItem(objectId, EventItemActivity.class);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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

    public ProfileData getImageProfile(String senderId) {
        ProfileData profileData = new ProfileData(new JSONObject(), Const.PIC_SQUARE);
        for (ProfileData object : profileList) {
            if (object.getUid().equals(senderId)) {
                profileData = object;
            }
        }
        return profileData;
    }

    private ArrayList<ProfileData> parseProfileList(JSONObject jsonObject) {
        ArrayList<ProfileData> resultList = new ArrayList<ProfileData>();
        try {
            JSONArray array = jsonObject.getJSONArray(Const.FQL_RESULT_SET);
            for (int i = 0; i < array.length(); i++) {
                resultList.add(new ProfileData(array.getJSONObject(i), Const.PIC_SQUARE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private ArrayList<NotificationData> parseNotifications(JSONObject jsonObject) {
        ArrayList<NotificationData> resultList = new ArrayList<NotificationData>();
        try {
            JSONArray array = jsonObject.getJSONArray(Const.FQL_RESULT_SET);
            for (int i = 0; i < array.length(); i++) {
                resultList.add(new NotificationData(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}