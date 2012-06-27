package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.InBoxActivity;
import jp.katahirado.android.kaopoto.model.CommentData;
import jp.katahirado.android.kaopoto.model.MessageThreadData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class InBoxAdapter extends FacebookBaseAdapter {

    private ArrayList<MessageThreadData> messageThreadList;
    private MessageThreadData messageThreadData;
    private TextView updatedTimeText;
    private InBoxActivity activity;

    public InBoxAdapter(InBoxActivity context, ArrayList<MessageThreadData> list) {
        super(context);
        activity = context;
        messageThreadList = list;
    }

    @Override
    public int getCount() {
        return messageThreadList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageThreadList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        messageThreadData = messageThreadList.get(position);
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.in_box_row, null);
        }
        profile_pic = (ImageView) view.findViewById(R.id.picture);
        firstText = (TextView) view.findViewById(R.id.name);
        firstText.setText(messageThreadData.getToUsersName());
        secondText = (TextView) view.findViewById(R.id.message);
        CommentData commentData = messageThreadData.getLastCommentData();
        if (commentData != null) {
            String uid = commentData.getFromUser().getUid();
            String fromPic = activity.getImageURLFromDB(uid);
            profile_pic.setImageBitmap(Utility.model.getImage(uid, fromPic));
            secondText.setText(commentData.getMessage());
        } else {
            profile_pic.setImageBitmap(null);
            secondText.setText("");
        }
        updatedTimeText = (TextView) view.findViewById(R.id.updated_time);
        updatedTimeText.setText(KaopotoUtil.formattedDateString(messageThreadData.getUpdatedTime()));
        return view;
    }
}
