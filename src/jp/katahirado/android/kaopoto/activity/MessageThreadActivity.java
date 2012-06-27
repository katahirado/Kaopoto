package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.adapter.MessageListAdapter;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.MessageThreadData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageThreadActivity extends Activity
        implements View.OnClickListener {
    private EditText messageText;
    private MessageThreadData messageThreadData;
    private MessageListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_thread);

        ListView commentsListView = (ListView) findViewById(R.id.comments_listview);
        messageText = (EditText) findViewById(R.id.message_text);
        Button button = (Button) findViewById(R.id.message_button);

        try {
            messageThreadData =
                    new MessageThreadData(new JSONObject(getIntent().getStringExtra(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<CommentData> commentLists = new ArrayList<CommentData>();
        commentLists.add(new CommentData(messageThreadData));
        commentLists.addAll(messageThreadData.getComments());
        adapter = new MessageListAdapter(this,commentLists);
        commentsListView.setAdapter(adapter);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_button:
                break;
        }
    }
}