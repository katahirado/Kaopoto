package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.model.EventData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class EventItemActivity extends Activity {
    private EventData eventData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_item);
        TextView name = (TextView) findViewById(R.id.event_item_name);
        TextView privacy = (TextView) findViewById(R.id.event_item_privacy);
        TextView ownerName = (TextView) findViewById(R.id.event_item_owner_name);
        TextView startAndEndTime = (TextView) findViewById(R.id.event_item_start_end);
        TextView location = (TextView) findViewById(R.id.event_item_location);
        TextView description = (TextView) findViewById(R.id.event_item_description);

        Bundle extras = getIntent().getExtras();
        try {
            eventData = new EventData(new JSONObject(extras.getString(Const.API_RESPONSE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name.setText(eventData.getName());
        privacy.setText(eventData.getPrivacy() + " : ");
        ownerName.setText(eventData.getOwner().getName());
        startAndEndTime.setText("日時 : " + eventData.getStartAndEnd());
        location.setText("場所 : " + eventData.getLocation());
        description.setText(eventData.getDescription());
    }
}