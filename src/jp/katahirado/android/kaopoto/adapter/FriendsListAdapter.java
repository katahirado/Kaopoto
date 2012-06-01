package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.FriendsListActivity;
import jp.katahirado.android.kaopoto.model.ProfileData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendsListAdapter extends FacebookBaseAdapter {

    private ArrayList<ProfileData> friendsList;
    private ProfileData friendData;

    public FriendsListAdapter(FriendsListActivity context, ArrayList<ProfileData> friendsList) {
        super(context);
        this.friendsList = friendsList;
    }

    @Override
    public int getCount() {
        return friendsList.size();
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
