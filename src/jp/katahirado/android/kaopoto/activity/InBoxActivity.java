package jp.katahirado.android.kaopoto.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.InBoxAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.MessageThreadData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class InBoxActivity extends ListActivity {

    private InBoxAdapter adapter;
    private ProfilesDao profilesDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_box);

        profilesDao = new ProfilesDao(new DBOpenHelper(this).getReadableDatabase());
        ArrayList<MessageThreadData> messageThreadList =
                parseMessageThread(getIntent().getStringExtra(Const.API_RESPONSE));
        adapter = new InBoxAdapter(this, messageThreadList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private ArrayList<MessageThreadData> parseMessageThread(String response) {
        ArrayList<MessageThreadData> resultList = new ArrayList<MessageThreadData>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray(Const.DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                resultList.add(new MessageThreadData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public String getImageURLFromDB(String fromUid) {
        return profilesDao.getImageUrl(fromUid);
    }
}