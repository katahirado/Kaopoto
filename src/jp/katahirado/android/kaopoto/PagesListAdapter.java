package jp.katahirado.android.kaopoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.FriendsGetProfilePics;
import com.facebook.android.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PagesListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private JSONArray _jsonArray;
    ImageView profile_pic;
    TextView name;
    TextView category_text;

    public PagesListAdapter(PagesListActivity context, JSONArray jsonArray) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        Utility.model.setListener(this);
        _jsonArray = jsonArray;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _jsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
        name = (TextView) view.findViewById(R.id.page_name);
        category_text = (TextView) view.findViewById(R.id.page_category);
        try {
            profile_pic.setImageBitmap(Utility.model.getImage(
                    jsonObject.getString(Const.ID), jsonObject.getString(Const.PICTURE)));
        } catch (JSONException e) {
            name.setText("");
        }
        try {
            name.setText(jsonObject.getString(Const.NAME));
        } catch (JSONException e) {
            name.setText("");
        }
        try {
            category_text.setText(jsonObject.getString(Const.CATEGORY));
        } catch (JSONException e) {
            category_text.setText("");
        }
        return view;
    }
}
