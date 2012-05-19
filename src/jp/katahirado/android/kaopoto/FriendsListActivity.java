package jp.katahirado.android.kaopoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        setTitle(getString(R.string.app_name) + " : Friends");
        listView = (ListView) findViewById(R.id.friendslist);
        Button searchButton = (Button) findViewById(R.id.friends_search_button);
        searchText = (EditText) findViewById(R.id.friends_search_text);

        intent = getIntent();

        Bundle extras = intent.getExtras();
        try {
            jsonArray = new JSONObject(extras.getString(Const.API_RESPONSE)).getJSONArray(Const.DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }
        JsonManager.mJsonArray = jsonArray;
        listView.setAdapter(new FriendsListAdapter(this, jsonArray));
        listView.setOnItemClickListener(this);
        listView.requestFocus();

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
        String friendId = "me";
        try {
            friendId = jsonArray.getJSONObject(position).getString(Const.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.FACEBOOK_PROF_URL + friendId));
        startActivity(intent);
    }


    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}