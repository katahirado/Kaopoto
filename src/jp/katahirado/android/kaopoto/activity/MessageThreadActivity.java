package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.MessageListAdapter;
import jp.katahirado.android.kaopoto.dao.DBOpenHelper;
import jp.katahirado.android.kaopoto.dao.ProfilesDao;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.MessageThreadData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageThreadActivity extends Activity {
    //        implements View.OnClickListener {
//    private EditText messageText;
    private MessageThreadData messageThreadData;
    private MessageListAdapter adapter;
    public ProfilesDao profilesDao;
//    private String message;
//    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_thread);

        ListView commentsListView = (ListView) findViewById(R.id.comments_listview);
//        messageText = (EditText) findViewById(R.id.message_text);
//        Button button = (Button) findViewById(R.id.message_button);
        profilesDao = new ProfilesDao(new DBOpenHelper(this).getReadableDatabase());

        try {
            messageThreadData =
                    new MessageThreadData(new JSONObject(getIntent().getStringExtra(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<CommentData> commentLists = new ArrayList<CommentData>();
        commentLists.add(new CommentData(messageThreadData));
        commentLists.addAll(messageThreadData.getComments());
        adapter = new MessageListAdapter(this, commentLists);
        commentsListView.setAdapter(adapter);

//        button.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.message_button:
//                message = messageText.getText().toString();
//                if (message.isEmpty()) {
//                    return;
//                }
//                dialog = ProgressDialog.show(this, "",
//                        getString(jp.katahirado.android.kaopoto.R.string.loading), true, true);
//                Bundle params = new Bundle();
//                params.putString(Const.MESSAGE, message);
//                Utility.mAsyncRunner.request(messageThreadData.getThreadId() + "/" + Const.COMMENTS, params,
//                        Const.POST, new BaseRequestListener() {
//                    @Override
//                    public void onComplete(final String response, final Object state) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    adapter.add(new CommentData(new JSONObject(response)));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                messageText.setText("");
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                }, null);
//                break;
//        }
//    }
}