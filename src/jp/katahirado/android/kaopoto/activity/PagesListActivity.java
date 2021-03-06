package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import jp.katahirado.android.kaopoto.adapter.PagesListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.PageData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PagesListActivity extends Activity implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private EditText searchText;
    private ArrayList<PageData> pageList;
    private PagesListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pages_list);
        setTitle(getString(R.string.app_name) + " : Pages");
        listView = (ListView) findViewById(R.id.pageslist);
        Button searchButton = (Button) findViewById(R.id.pages_search_button);
        searchText = (EditText) findViewById(R.id.pages_search_text);

        pageList = KaopotoUtil.parsePageList(getIntent().getStringExtra(Const.API_RESPONSE));
        adapter = new PagesListAdapter(this, pageList);
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
        final ProfilesDao profilesDao = new ProfilesDao(new DBOpenHelper(this).getWritableDatabase());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                profilesDao.vacuum();
            }
        })).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(KaopotoUtil.getProfileURL(adapter.getItem(position).getUid()))));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pages_search_button:
                SpannableStringBuilder builder = (SpannableStringBuilder) searchText.getText();
                String query = builder.toString().trim().toLowerCase();
                adapter = new PagesListAdapter(this, pageListFilter(query));
                listView.setAdapter(adapter);
                searchText.setText("");
                hideIME();
                break;
        }
    }

    private ArrayList<PageData> pageListFilter(String query) {
        if (query.length() == 0) {
            setTitle(getString(R.string.app_name) + " : Pages");
            return pageList;
        }
        setTitle(getString(R.string.app_name) + " : Pages Search : " + query);
        ArrayList<PageData> resultList = new ArrayList<PageData>();
        for (PageData object : pageList) {
            if (object.getName().toLowerCase().contains(query)) {
                resultList.add(object);
            }
        }
        return resultList;
    }

    private void hideIME() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}