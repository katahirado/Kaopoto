package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.NotificationsActivity;
import jp.katahirado.android.kaopoto.model.NotificationData;
import jp.katahirado.android.kaopoto.model.ProfileData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationsAdapter extends FacebookBaseAdapter {

    private ArrayList<NotificationData> notificationList;
    private NotificationData notificationData;
    private ProfileData profileData;
    private NotificationsActivity activity;

    public NotificationsAdapter(NotificationsActivity context, ArrayList<NotificationData> list) {
        super(context);
        activity = context;
        notificationList = list;
    }

    @Override
    public int getCount() {
        return notificationList.size();
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
        profile_pic.setImageBitmap(Utility.model.getImage(notificationData.getSenderId(),
                profileData.getPicture()));
        firstText.setText(notificationData.getTitleText());
        return view;
    }
}
