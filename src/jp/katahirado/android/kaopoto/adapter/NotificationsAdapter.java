package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.JsonManager;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.NotificationsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsAdapter extends FacebookBaseAdapter {

    public NotificationsAdapter(NotificationsActivity context, JSONArray jsonArray) {
        super(context, jsonArray);
    }

    @Override
    public int getCount() {
        return _jsonArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        JSONObject jsonObject = null;
        JSONObject imageObject = null;
        String senderId = "";
        try {
            jsonObject = _jsonArray.getJSONObject(position);
            senderId = jsonObject.getString(Const.SENDER_ID);
            imageObject = JsonManager.getImageObject(senderId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.notification_item, null);
        }

        profile_pic = (ImageView) view.findViewById(R.id.notification_pic);
        firstText = (TextView) view.findViewById(R.id.notification_title);

        try {
            profile_pic.setImageBitmap(Utility.model.getImage(senderId,
                    imageObject.getString(Const.PIC_SQUARE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            firstText.setText(jsonObject.getString("title_text"));
        } catch (JSONException e) {
            firstText.setText("");
        }
        return view;
    }
}
