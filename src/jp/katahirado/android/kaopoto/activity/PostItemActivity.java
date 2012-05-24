package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.*;
import jp.katahirado.android.kaopoto.model.PostData;
import jp.katahirado.android.kaopoto.model.UserData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PostItemActivity extends Activity {
    private PostData postData;
    private ImageView postFromPicView;
    private DBOpenHelper dbHelper;
    private String fromUid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try {
            postData = new PostData(new JSONObject(extras.getString(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView message = (TextView) findViewById(R.id.post_item_message);
        postFromPicView = (ImageView) findViewById(R.id.post_item_from_pic);
        fromUid = postData.getFromUser().getUid();
        String fromUserName = postData.getFromUser().getName()+"->";
        if(postData.getToUsers().size()>0){
            for(UserData data:postData.getToUsers()){
                fromUserName+= data.getName()+" :";
            }
        }
        message.setText(fromUserName+postData.getMessage());
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