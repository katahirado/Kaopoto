package jp.katahirado.android.kaopoto.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.InBoxAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.MessageThreadData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class InBoxActivity extends ListActivity {

    private InBoxAdapter adapter;
    public ProfilesDao profilesDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_box);

        profilesDao = new ProfilesDao(new DBOpenHelper(this).getReadableDatabase());
        ArrayList<MessageThreadData> messageThreadList =
                KaopotoUtil.parseMessageThread(getIntent().getStringExtra(Const.API_RESPONSE));
        adapter = new InBoxAdapter(this, messageThreadList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
        MessageThreadData threadData = adapter.getItem(position);
        Bundle params = new Bundle();
        params.putString(Const.DATE_FORMAT, "U");
        Utility.mAsyncRunner.request(threadData.getThreadId(), params, new BaseRequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MessageThreadActivity.class);
                intent.putExtra(Const.API_RESPONSE, response);
                startActivity(intent);
            }
        });
    }
}