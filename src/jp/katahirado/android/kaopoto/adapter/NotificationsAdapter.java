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
import jp.katahirado.android.kaopoto.activity.NotificationsActivity;
import jp.katahirado.android.kaopoto.model.ProfileData;
import jp.katahirado.android.kaopoto.model.NotificationData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<NotificationData> notificationList;
    private ImageView profile_pic;
    private TextView firstText;
    private NotificationData notificationData;
    private ArrayList<ProfileData> profileList;
    private ProfileData profileData;
    private NotificationsActivity activity;

    public NotificationsAdapter(NotificationsActivity context,
                                ArrayList<NotificationData> notificationList,
                                ArrayList<ProfileData> profileList) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        Utility.model.setListener(this);
        this.activity = context;
        this.notificationList = notificationList;
        this.profileList = profileList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notificationList.size();
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
        notificationData = notificationList.get(position);
        profileData = activity.getImageProfile(notificationData.getSenderId());
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.notification_row, null);
        }

        profile_pic = (ImageView) view.findViewById(R.id.notification_pic);
        firstText = (TextView) view.findViewById(R.id.notification_title);

        profile_pic.setImageBitmap(Utility.model.getImage(notificationData.getSenderId(), profileData.getPicture()));
        firstText.setText(notificationData.getTitleText());
        return view;
    }
}
