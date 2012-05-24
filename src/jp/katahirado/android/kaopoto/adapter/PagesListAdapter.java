package jp.katahirado.android.kaopoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.FriendsGetProfilePics;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.PagesListActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PagesListAdapter extends FacebookBaseAdapter {

    public PagesListAdapter(PagesListActivity context, JSONArray jsonArray) {
        super(context,jsonArray);
    }

    @Override
    public int getCount() {
        return _jsonArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        JSONObject jsonObject = null;
        try {
            jsonObject = _jsonArray.getJSONObject(position);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.page_item, null);
        }

        profile_pic = (ImageView) view.findViewById(R.id.page_profile_pic);
        firstText = (TextView) view.findViewById(R.id.page_name);
        secondText = (TextView) view.findViewById(R.id.page_category);
        try {
            profile_pic.setImageBitmap(Utility.model.getImage(
                    jsonObject.getString(Const.ID), jsonObject.getString(Const.PICTURE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            firstText.setText(jsonObject.getString(Const.NAME));
        } catch (JSONException e) {
            firstText.setText("");
        }
        try {
            secondText.setText(jsonObject.getString(Const.CATEGORY));
        } catch (JSONException e) {
            secondText.setText("");
        }
        return view;
    }
}
