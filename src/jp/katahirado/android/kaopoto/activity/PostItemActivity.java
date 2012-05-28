package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.DBOpenHelper;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.SQLiteManager;
import jp.katahirado.android.kaopoto.adapter.CommentsListAdapter;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.PostData;
import jp.katahirado.android.kaopoto.model.UserData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PostItemActivity extends Activity implements View.OnClickListener {
    private PostData postData;
    private ImageView postFromPicView;
    private DBOpenHelper dbHelper;
    private String fromUid;
    private ProgressDialog dialog;
    private Bundle params;
    private Intent intent;
    private EditText commentText;
    private String message;
    private CommentsListAdapter adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);

        TextView message = (TextView) findViewById(R.id.post_item_message);
        postFromPicView = (ImageView) findViewById(R.id.post_item_from_pic);
        ImageView mediaView = (ImageView) findViewById(R.id.media_item);
        TextView name = (TextView) findViewById(R.id.post_item_name);
        TextView caption = (TextView) findViewById(R.id.post_item_caption);
        TextView description = (TextView) findViewById(R.id.post_item_description);
        TextView likesCountAndUsers = (TextView) findViewById(R.id.post_likes_count_and_users_text);
        ListView commentsList = (ListView) findViewById(R.id.post_comments_list_view);
        Button likeButton = (Button) findViewById(R.id.post_like_button);
        commentText = (EditText) findViewById(R.id.post_comment_text);
        Button commentButton = (Button) findViewById(R.id.post_comment_button);

        intent = getIntent();
        Bundle extras = intent.getExtras();
        try {
            postData = new PostData(new JSONObject(extras.getString(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fromUid = postData.getFromUser().getUid();
        String fromUserName = postData.getFromUser().getName() + "->";
        if (postData.getToUsers().size() > 0) {
            for (UserData data : postData.getToUsers()) {
                fromUserName += data.getName() + " :";
            }
        }
        message.setText(fromUserName + postData.getMessage());

        if (postData.getName().isEmpty()) {
            name.setVisibility(View.GONE);
        }
        name.setText(postData.getName());

        if (postData.getCaption().isEmpty()) {
            caption.setVisibility(View.GONE);
        }
        caption.setText(postData.getCaption());

        if (postData.getDescription().isEmpty()) {
            description.setVisibility(View.GONE);
        }
        description.setText(postData.getDescription());

        if (postData.getPicture().isEmpty()) {
            mediaView.setVisibility(View.GONE);
        }
        mediaView.setImageBitmap(Utility.getBitmap(postData.getPicture()));

        if (postData.getLikesCount() == 0) {
            likesCountAndUsers.setVisibility(View.GONE);
        } else {
            String s = String.valueOf(postData.getLikesCount()) + "人がいいね!";
            for (UserData u : postData.getLikes()) {
                s = s + "," + u.getName();
            }
            likesCountAndUsers.setText(s);
        }

        if (postData.getCommentsCount() == 0) {
            commentsList.setVisibility(View.GONE);
        }
        commentButton.setOnClickListener(this);
        adapter = new CommentsListAdapter(this,postData.getComments());
        commentsList.setAdapter(adapter);

        //videoのthumbnail生成
        //  ThumbnailUtils utils= new ThumbnailUtils();

        dbHelper = new DBOpenHelper(this);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                String fromPic = SQLiteManager.getImageUrl(database, fromUid);
                database.close();
                GetProfilePicTask getProfilePicTask = new GetProfilePicTask();
                getProfilePicTask.execute(fromPic);
            }
        })).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_comment_button:
                message = commentText.getText().toString();
                if (message.isEmpty()) {
                    return;
                }
                dialog = ProgressDialog.show(this, "",
                        getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                params = new Bundle();
                params.putString(Const.MESSAGE, message);
                Utility.mAsyncRunner.request(postData.getPostId() + "/" + Const.COMMENTS, params,
                        Const.POST, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String response, final Object state) {
                        dialog.dismiss();
                        //CommentData生成
                        SQLiteDatabase database = dbHelper.getReadableDatabase();
                        String userName = SQLiteManager.getUserName(database,Utility.userUID);
                        UserData fromData= new UserData(Utility.userUID,userName);
                        database.close();
                        CommentData.buildCommentData(response, message,fromData);
                    }
                }, null);
                break;
        }
    }

    private class GetProfilePicTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return Utility.getBitmap(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            postFromPicView.setImageBitmap(bitmap);
        }
    }
}