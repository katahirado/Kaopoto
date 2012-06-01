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
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.FriendsListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.model.ProfileData;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendsListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText searchText;
    private ListView listView;
    private ProfilesDao profilesDao;
    private ArrayList<ProfileData> friendsList;
    private FriendsListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        setTitle(getString(R.string.app_name) + " : Friends");
        listView = (ListView) findViewById(R.id.friendslist);
        Button searchButton = (Button) findViewById(R.id.friends_search_button);
        searchText = (EditText) findViewById(R.id.friends_search_text);

        Bundle extras = getIntent().getExtras();
        friendsList = parseFriends(extras.getString(Const.API_RESPONSE));
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                profilesDao.bulkInsert(friendsList);
            }
        })).start();
        adapter = new FriendsListAdapter(this,friendsList);
        listView.setAdapter(adapter);
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
                adapter = new FriendsListAdapter(this,friendsListFilter(query));
                listView.setAdapter(adapter);
                searchText.setText("");
                setTitle(getString(R.string.app_name) + " : Friends Search : " + query);
                hideIME();
                break;
        }
    }

    private ArrayList<ProfileData> friendsListFilter(String query) {
        if (query.equals("*")) {
            return friendsList;
        }
        ArrayList<ProfileData> resultList = new ArrayList<ProfileData>();
        for (ProfileData object : friendsList) {
            String name;
            if (query.matches("^[0-9/]*")) {
                name = object.getBirthday();
            } else {
                name = object.getName().toLowerCase();
            }
            if (name.contains(query)) {
                resultList.add(object);
            }
        }
        return resultList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Bundle params = new Bundle();
        params.putString(Const.TO, adapter.getItem(position).getUid());
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

    private ArrayList<ProfileData> parseFriends(String response) {
        ArrayList<ProfileData> resultList = new ArrayList<ProfileData>();
        try {
            JSONArray jArray = new JSONObject(response).getJSONArray(Const.DATA);
            for (int i = 0; i < jArray.length(); i++) {
                resultList.add(new ProfileData(jArray.getJSONObject(i),Const.PICTURE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}