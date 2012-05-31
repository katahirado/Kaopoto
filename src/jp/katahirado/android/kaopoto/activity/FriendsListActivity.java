package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.facebook.android.BaseDialogListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.*;
import jp.katahirado.android.kaopoto.adapter.FriendsListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendsListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private JSONArray jsonArray;
    private EditText searchText;
    private ListView listView;
    private ProfilesDao profilesDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        setTitle(getString(R.string.app_name) + " : Friends");
        listView = (ListView) findViewById(R.id.friendslist);
        Button searchButton = (Button) findViewById(R.id.friends_search_button);
        searchText = (EditText) findViewById(R.id.friends_search_text);

        Bundle extras = getIntent().getExtras();
        try {
            jsonArray = new JSONObject(extras.getString(Const.API_RESPONSE)).getJSONArray(Const.DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }
        JsonManager.mJsonArray = jsonArray;
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                JsonManager.setAList(Const.PICTURE);
                profilesDao.bulkInsert();
            }
        })).start();
        listView.setAdapter(new FriendsListAdapter(this, jsonArray));
        listView.setOnItemClickListener(this);
        listView.requestFocus();
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideIME();
                return false;
            }
        });

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friends_search_button:
                SpannableStringBuilder builder = (SpannableStringBuilder) searchText.getText();
                String query = builder.toString().trim().toLowerCase();
                if (query.length() == 0) {
                    return;
                }
                jsonArray = JsonManager.querySearchFriends(query);
                listView.setAdapter(new FriendsListAdapter(this, jsonArray));
                searchText.setText("");
                setTitle(getString(R.string.app_name) + " : Friends Search : " + query);
                hideIME();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Bundle params = new Bundle();
        long friendId = 0;
        try {
            friendId = jsonArray.getJSONObject(position).getLong(Const.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.putString(Const.TO, String.valueOf(friendId));
        params.putString(Const.CAPTION, getString(jp.katahirado.android.kaopoto.R.string.app_name));
        Utility.mFacebook.dialog(this, Const.FEED, params, new BaseDialogListener() {
            @Override
            public void onComplete(Bundle values) {
                final String postId = values.getString(Const.POST_ID);
                if (postId != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.PostedOnTheWall),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}