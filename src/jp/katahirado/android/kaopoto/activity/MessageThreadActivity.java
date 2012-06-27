package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.model.MessageThreadData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageThreadActivity extends Activity
        implements View.OnClickListener {
    private EditText messageText;
    private MessageThreadData messageThreadData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_thread);

        ListView commentsListView = (ListView) findViewById(R.id.comments_listview);
        messageText = (EditText) findViewById(R.id.message_text);
        Button button = (Button) findViewById(R.id.message_button);
        TextView fromUserName = (TextView) findViewById(R.id.from_user_name);
        TextView fromMessage = (TextView) findViewById(R.id.from_message);
        TextView updatedTime = (TextView) findViewById(R.id.updated_time);
        try {
            messageThreadData =
                    new MessageThreadData(new JSONObject(getIntent().getStringExtra(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fromUserName.setText(messageThreadData.getFromUser().getName());
        fromMessage.setText(messageThreadData.getMessage());
        updatedTime.setText(KaopotoUtil.formattedDateString(messageThreadData.getUpdatedTime()));
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