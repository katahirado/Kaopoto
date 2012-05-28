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
public class PostItemActivity extends Activity implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private PostData postData;
    private ImageView postFromPicView;
    private ListView commentsList;
    private DBOpenHelper dbHelper;
    private ProgressDialog dialog;
    private EditText commentText;
    private String message;
    private TextView likesCountAndUsers;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);

        TextView message = (TextView) findViewById(R.id.post_item_message);
        postFromPicView = (ImageView) findViewById(R.id.post_item_from_pic);
        ImageView mediaView = (ImageView) findViewById(R.id.media_item);
        TextView name = (TextView) findViewById(R.id.post_item_name);
        TextView caption = (TextView) findViewById(R.id.post_item_caption);
        TextView description = (TextView) findViewById(R.id.post_item_description);
        likesCountAndUsers = (TextView) findViewById(R.id.post_likes_count_and_users_text);
        commentsList = (ListView) findViewById(R.id.post_comments_list_view);
        Button likeButton = (Button) findViewById(R.id.post_like_button);
        commentText = (EditText) findViewById(R.id.post_comment_text);
        Button commentButton = (Button) findViewById(R.id.post_comment_button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try {
            postData = new PostData(new JSONObject(extras.getString(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        populateLikeCountAndUserText();
        setCommentsListAdapter();

        commentButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        commentsList.setOnItemClickListener(this);

        //videoのthumbnail生成
        //  ThumbnailUtils utils= new ThumbnailUtils();

        dbHelper = new DBOpenHelper(this);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                String fromPic = getImageURLFromDB(postData.getFromUser().getUid());
                GetProfilePicTask getProfilePicTask = new GetProfilePicTask();
                getProfilePicTask.execute(fromPic);
            }
        })).start();
    }

    @Override
    public void onClick(View view) {
        Bundle params;
        switch (view.getId()) {
            case R.id.post_like_button:
                params = new Bundle();
                if (!isLike()) {
                    params.putString("method", "delete");
                }
                dialog = ProgressDialog.show(this, "",
                        getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
                Utility.mAsyncRunner.request(postData.getPostId() + "/" + Const.LIKES, params,
                        Const.POST, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String response, final Object state) {
                        dialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postData.increaseOrDecreaseLikesCount(isLike());
                                postData.addOrRemoveLike(getUserDataFromDB(), isLike());
                                populateLikeCountAndUserText();
                            }
                        });
                    }
                }, null);
                break;
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postData.incrementCommentsCount();
                                postData.addComment(CommentData.buildCommentData(response, message,
                                        getUserDataFromDB()));
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
        Utility.mAsyncRunner.request(postData.getComments().get(position).getCommentId() + "/" + Const.LIKES,
                new Bundle(), Const.POST, new BaseRequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                //ここで再びcomments全体を再取得する
                Utility.mAsyncRunner.request(postData.getPostId() + "/" + Const.COMMENTS, new BaseRequestListener() {
                    @Override
                    public void onComplete(final String res, Object state) {
                        dialog.dismiss();
                        //adapterに再セットする
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postData.setComments(res);
                                setCommentsListAdapter();
                            }
                        });
                    }
                });
            }
        }, null);
    }

    private void populateLikeCountAndUserText() {
        if (postData.getLikesCount() == 0) {
            likesCountAndUsers.setVisibility(View.GONE);
        } else {
            String s = String.valueOf(postData.getLikesCount()) + getString(R.string.people_likes);
            for (UserData u : postData.getLikes()) {
                s = s + "," + u.getName();
            }
            likesCountAndUsers.setVisibility(View.VISIBLE);
            likesCountAndUsers.setText(s);
        }
    }

    private void setCommentsListAdapter() {
        if (postData.getCommentsCount() == 0) {
            commentsList.setVisibility(View.GONE);
        } else {
            commentsList.setVisibility(View.VISIBLE);
        }
        CommentsListAdapter adapter = new CommentsListAdapter(this, postData.getComments());
        commentsList.setAdapter(adapter);
    }

    private UserData getUserDataFromDB() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String userName = SQLiteManager.getUserName(database, Utility.userUID);
        //        database.close();
        return new UserData(Utility.userUID, userName);
    }

    public String getImageURLFromDB(String fromUid) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //        database.close();
        return SQLiteManager.getImageUrl(database, fromUid);
    }

    private boolean isLike() {
        boolean result = true;
        for (UserData data : postData.getLikes()) {
            if (data.getUid().equals(Utility.userUID)) {
                result = false;
            }
        }
        return result;
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