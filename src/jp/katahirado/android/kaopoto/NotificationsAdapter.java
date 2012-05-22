package jp.katahirado.android.kaopoto;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsAdapter extends FacebookBaseAdapter {
    private JSONArray profileImages;

    public NotificationsAdapter(NotificationsActivity context, JSONArray jsonArray, JSONArray images) {
        super(context, jsonArray);
        profileImages = images;
    }

    @Override
    public int getCount() {
        return _jsonArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        JSONObject jsonObject = null;
        JSONObject imageObject = null;
        try {
            jsonObject = _jsonArray.getJSONObject(position);
            imageObject = profileImages.getJSONObject(position);
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
            profile_pic.setImageBitmap(Utility.model.getImage(jsonObject.getString("sender_id"),
                    imageObject.getString("pic_square")));
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
