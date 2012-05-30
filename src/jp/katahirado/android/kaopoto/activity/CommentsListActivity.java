package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.DBOpenHelper;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.SQLiteManager;
import jp.katahirado.android.kaopoto.adapter.CommentsListAdapter;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.UserData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentsListActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView commentsListView;
    private EditText commentText;
    private ArrayList<CommentData> comments;
    private DBOpenHelper dbHelper;
    private DialogInterface dialog;
    private String postItemId;
    private String message;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_list);
        commentsListView = (ListView) findViewById(R.id.comments_list_listview);
        commentText = (EditText) findViewById(R.id.comments_list_post_text);
        Button commentButton = (Button) findViewById(R.id.comments_list_post_button);
        dbHelper = new DBOpenHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        comments = parseComments(extras.getString(Const.API_RESPONSE));
        postItemId = extras.getString(Const.ID);
        setCommentsListAdapter();
        commentsListView.setOnItemClickListener(this);

        commentButton.setOnClickListener(this);

    }

    private void setCommentsListAdapter() {
        CommentsListAdapter adapter = new CommentsListAdapter(this, comments);
        commentsListView.setAdapter(adapter);
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
                        dialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addComment(CommentData.buildCommentData(response, message, getUserDataFromDB()));
                                setCommentsListAdapter();
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
        Utility.mAsyncRunner.request(comments.get(position).getCommentId() + "/" + Const.LIKES,
                new Bundle(), Const.POST, new BaseRequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                //ここで再びcomments全体を再取得する
                Utility.mAsyncRunner.request(postItemId + "/" + Const.COMMENTS, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String res, Object state) {
                        dialog.dismiss();
                        //adapterに再セットする
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parseComments(res);
                                setCommentsListAdapter();
                            }
                        });
                    }
                });
            }
        }, null);
    }

    private UserData getUserDataFromDB() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String userName = SQLiteManager.getUserName(database, Utility.userUID);
        return new UserData(Utility.userUID, userName);
    }

    public String getImageURLFromDB(String fromUid) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return SQLiteManager.getImageUrl(database, fromUid);
    }

    private ArrayList<CommentData> parseComments(String response) {
        ArrayList<CommentData> resultList;
        try {
            JSONArray cArray = new JSONObject(response).getJSONArray(Const.DATA);
            resultList = new ArrayList<CommentData>();
            for (int i = 0; i < cArray.length(); i++) {
                resultList.add(new CommentData(cArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            resultList = new ArrayList<CommentData>();
        }
        return resultList;
    }

    private void addComment(CommentData commentData) {
        comments.add(commentData);
    }

}