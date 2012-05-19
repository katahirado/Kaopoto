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
public class PagesListActivity extends Activity implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private EditText searchText;
    private Intent intent;
    private JSONArray jsonArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pages_list);
        setTitle(getString(R.string.app_name) + " : Pages");
        listView = (ListView) findViewById(R.id.pageslist);
        Button searchButton = (Button) findViewById(R.id.pages_search_button);
        searchText = (EditText) findViewById(R.id.pages_search_text);

        intent = getIntent();

        Bundle extras = intent.getExtras();
        try {
            jsonArray = new JSONObject(extras.getString(Const.API_RESPONSE)).getJSONArray(Const.DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }
        JsonManager.mJsonArray = jsonArray;
        listView.setAdapter(new PagesListAdapter(this, jsonArray));
        listView.setOnItemClickListener(this);
        listView.requestFocus();

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String pageId = "me";
        try {
            pageId = jsonArray.getJSONObject(position).getString(Const.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.FACEBOOK_PROF_URL + pageId));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pages_search_button:
                SpannableStringBuilder builder = (SpannableStringBuilder) searchText.getText();
                String query = builder.toString().trim().toLowerCase();
                if (query.length() == 0) {
                    return;
                }
                jsonArray = JsonManager.querySearchPages(query);
                listView.setAdapter(new PagesListAdapter(this, jsonArray));
                searchText.setText("");
                setTitle(getString(R.string.app_name) + " : Pages Search : " + query);
                hideIME();
                break;
        }
    }

    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}