package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.CommentsListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.UserData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentsListActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText commentText;
    private DialogInterface dialog;
    private String postItemId;
    private String message;
    private CommentsListAdapter adapter;
    public ProfilesDao profilesDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_list);
        ListView commentsListView = (ListView) findViewById(R.id.comments_list_listview);
        commentText = (EditText) findViewById(R.id.comments_list_post_text);
        Button commentButton = (Button) findViewById(R.id.comments_list_post_button);
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getReadableDatabase());

        Bundle extras = getIntent().getExtras();
        ArrayList<CommentData> comments = KaopotoUtil.parseComments(extras.getString(Const.API_RESPONSE));
        postItemId = extras.getString(Const.ID);
        adapter = new CommentsListAdapter(this, comments);
        commentsListView.setAdapter(adapter);

        commentsListView.setOnItemClickListener(this);
        commentButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Bundle params;
        switch (view.getId()) {
            case R.id.comments_list_post_button:
                message = commentText.getText().toString();
                if (message.isEmpty()) {
                    return;
                }
                dialog = ProgressDialog.show(this, "",
                        getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                params = new Bundle();
                params.putString(Const.MESSAGE, message);
                Utility.mAsyncRunner.request(postItemId + "/" + Const.COMMENTS, params,
                        Const.POST, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String response, final Object state) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(CommentData.buildCommentData(response, message, getUserDataFromDB()));
                                commentText.setText("");
                                dialog.dismiss();
                            }
                        });
                    }
                }, null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
        dialog = ProgressDialog.show(this, "",
                getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
        Utility.mAsyncRunner.request(adapter.getItem(position).getCommentId() + "/" + Const.LIKES,
                new Bundle(), Const.POST, new BaseRequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                //ここで再びcomments全体を再取得する
                Utility.mAsyncRunner.request(postItemId + "/" + Const.COMMENTS, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String res, Object state) {
                        //adapterに再セットする
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<CommentData> results = KaopotoUtil.parseComments(res);
                                adapter.addAll(results);
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }, null);
    }

    private UserData getUserDataFromDB() {
        String userName = profilesDao.getUserName(Utility.userUID);
        return new UserData(Utility.userUID, userName);
    }
}