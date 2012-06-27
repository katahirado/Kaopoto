package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.FriendsListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.ProfileData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendsListActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
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

        friendsList = KaopotoUtil.parseFriends(getIntent().getStringExtra(Const.API_RESPONSE));
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                profilesDao.bulkInsert(friendsList);
            }
        })).start();
        adapter = new FriendsListAdapter(this, friendsList);
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
                adapter = new FriendsListAdapter(this, friendsListFilter(query));
                listView.setAdapter(adapter);
                searchText.setText("");
                hideIME();
                break;
        }
    }

    private ArrayList<ProfileData> friendsListFilter(String query) {
        if (query.length() == 0) {
            setTitle(getString(R.string.app_name) + " : Friends");
            return friendsList;
        }
        setTitle(getString(R.string.app_name) + " : Friends Search : " + query);
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
        ProfileData profileData = adapter.getItem(position);
        Intent intent = new Intent(this, WallPostActivity.class);
        intent.putExtra(Const.TO, profileData.getUid());
        intent.putExtra(Const.NAME, profileData.getName());
        startActivity(intent);
    }

    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}