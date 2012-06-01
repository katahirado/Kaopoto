package jp.katahirado.android.kaopoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.FriendsGetProfilePics;
import com.facebook.android.Utility;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FacebookBaseAdapter extends BaseAdapter {
    protected LayoutInflater layoutInflater;

    protected ImageView profile_pic;
    protected TextView firstText;
    protected TextView secondText;

    public FacebookBaseAdapter(Activity context) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        Utility.model.setListener(this);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
