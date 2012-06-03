package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class WallPostActivity extends Activity implements View.OnClickListener {
    private EditText wallPostMessage;
    private String toId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_post);

        wallPostMessage = (EditText) findViewById(R.id.wall_post_message);
        TextView wallPostCaption = (TextView) findViewById(R.id.wall_post_caption);
        Button wallPostButton = (Button) findViewById(R.id.wall_post_button);
        TextView wallPostTo = (TextView) findViewById(R.id.wall_post_to);

        wallPostCaption.setText(getString(R.string.app_name) + "より");
        wallPostButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString(Const.MESSAGE);
            if (message != null) {
                wallPostMessage.setText(message);
            }
            String to = extras.getString(Const.TO);
            if (to != null) {
                toId = to;
            }
            String name = extras.getString(Const.NAME);
            if (name != null) {
                wallPostTo.setText("宛先 : " + name);
                wallPostTo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wall_post_button:
                String message = wallPostMessage.getText().toString();
                if (message.isEmpty()) {
                    return;
                }
                final ProgressDialog dialog = ProgressDialog.show(this, "",
                        getString(R.string.loading), true, true);
                Bundle params = new Bundle();
                if (toId != null) {
                    params.putString(Const.TO, toId);
                }
                params.putString(Const.MESSAGE, wallPostMessage.getText().toString());
                Utility.mAsyncRunner.request("me/feed", params, "POST",
                        new BaseRequestListener() {
                            @Override
                            public void onComplete(final String response, final Object state) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String postId = "";
                                        try {
                                            postId = new JSONObject(response).getString(Const.ID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (!postId.equals("")) {
                                            Toast.makeText(getApplicationContext(),
                                                    getString(jp.katahirado.android.kaopoto.R.string.PostedOnTheWall),
                                                    Toast.LENGTH_LONG).show();
                                            wallPostMessage.setText("");
                                            wallPostMessage.setVisibility(View.GONE);
                                            toId = null;
                                            finish();
                                        }
                                    }
                                });
                            }
                        }, null);
                break;
        }
    }
}