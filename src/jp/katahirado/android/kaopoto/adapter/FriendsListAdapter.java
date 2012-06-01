package jp.katahirado.android.kaopoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.FriendsGetProfilePics;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.FriendsListActivity;
import jp.katahirado.android.kaopoto.dao.ProfileData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendsListAdapter extends BaseAdapter {

    private FriendsListActivity activity;
    private ArrayList<ProfileData> friendsList;
    private LayoutInflater layoutInflater;
    private ImageView profile_pic;
    private TextView firstText;
    private TextView secondText;
    private ProfileData friendData;

    public FriendsListAdapter(FriendsListActivity context, ArrayList<ProfileData> friendsList) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        activity = context;
        Utility.model.setListener(this);
        this.friendsList = friendsList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        friendData = friendsList.get(position);
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.friend_row, null);
        }

        profile_pic = (ImageView) view.findViewById(R.id.friend_profile_pic);
        firstText = (TextView) view.findViewById(R.id.friend_name);
        secondText = (TextView) view.findViewById(R.id.friend_birthday_text);
        profile_pic.setImageBitmap(Utility.model.getImage(friendData.getUid(), friendData.getPicture()));
        firstText.setText(friendData.getName());
        secondText.setText(friendData.getBirthday());
        return view;
    }
}
