package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
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
    private ProgressDialog dialog;
    private TextView likesCountAndUsers;
    private Button commentViewButton;
    private String commentsResponse;
    private ProfilesDao profilesDao;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);

        TextView messageView = (TextView) findViewById(R.id.post_item_message);
        postFromPicView = (ImageView) findViewById(R.id.post_item_from_pic);
        ImageView mediaView = (ImageView) findViewById(R.id.media_item);
        TextView name = (TextView) findViewById(R.id.post_item_name);
        TextView caption = (TextView) findViewById(R.id.post_item_caption);
        TextView description = (TextView) findViewById(R.id.post_item_description);
        likesCountAndUsers = (TextView) findViewById(R.id.post_likes_count_and_users_text);
        Button likeButton = (Button) findViewById(R.id.post_like_button);
        commentViewButton = (Button) findViewById(R.id.post_comment_view_button);

        Bundle extras = getIntent().getExtras();
        try {
            postData = new PostData(new JSONObject(extras.getString(Const.API_RESPONSE)));
            commentsResponse = new JSONObject((extras.getString(Const.API_RESPONSE))).getString(Const.COMMENTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String fromUserName = postData.getFromUser().getName() + "->";
        if (postData.getToUsers().size() > 0) {
            for (UserData data : postData.getToUsers()) {
                fromUserName += data.getName() + " :";
            }
        }
        messageView.setText(fromUserName + postData.getMessage());

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
        populateCommentCount();
        likeButton.setOnClickListener(this);
        commentViewButton.setOnClickListener(this);

        //videoのthumbnail生成
        //  ThumbnailUtils utils= new ThumbnailUtils();

        profilesDao = new ProfilesDao(new DBOpenHelper(this).getReadableDatabase());
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
    protected void onResume() {
        super.onResume();
        Utility.mAsyncRunner.request(postData.getPostId() + "/" + Const.COMMENTS, new BaseRequestListener() {
            @Override
            public void onComplete(final String res, Object state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postData.setComments(res);
                        populateCommentCount();
                    }
                });
            }
        });
    }

    private void populateCommentCount() {
        int cCount = postData.getCommentsCount();
        if (cCount > 0) {
            commentViewButton
                    .setText(String.valueOf("コメント " + postData.getCommentsCount()) + "件 を見る");
        } else {
            commentViewButton.setText("コメント する");
        }
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
            case R.id.post_comment_view_button:
                Intent intent = new Intent(this, CommentsListActivity.class);
                intent.putExtra(Const.ID, postData.getPostId());
                intent.putExtra(Const.API_RESPONSE, commentsResponse);
                startActivity(intent);
                break;
        }
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

    private UserData getUserDataFromDB() {
        String userName = profilesDao.getUserName(Utility.userUID);
        return new UserData(Utility.userUID, userName);
    }

    public String getImageURLFromDB(String fromUid) {
        return profilesDao.getImageUrl(fromUid);
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