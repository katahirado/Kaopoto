package jp.katahirado.android.kaopoto;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PostItemActivity extends Activity {
    private PostData postData;
    private ImageView postFromPicView;

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

        message.setText(postData.getMessage());
        String fromUid = postData.getFromData().getUid();

        DBOpenHelper dbHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String fromPic = SQLiteManager.getImageUrl(database,fromUid);
        database.close();
        GetProfilePicTask getProfilePicTask = new GetProfilePicTask();
        getProfilePicTask.execute(fromUid,fromPic);
    }

    private class GetProfilePicTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            postFromPicView.setImageBitmap(bitmap);
        }
    }
}