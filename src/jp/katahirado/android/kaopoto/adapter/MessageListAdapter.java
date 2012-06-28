package jp.katahirado.android.kaopoto.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.MessageThreadActivity;
import jp.katahirado.android.kaopoto.model.CommentData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageListAdapter extends FacebookBaseAdapter {
    private MessageThreadActivity activity;
    private ArrayList<CommentData> commentsList;
    private CommentData commentData;
    private TextView createdTime;
    private Bitmap bmp;

    public MessageListAdapter(MessageThreadActivity context, ArrayList<CommentData> lists) {
        super(context);
        activity = context;
        commentsList = lists;
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public CommentData getItem(int position) {
        return commentsList.get(position);
    }

    public void add(CommentData commentData) {
        commentsList.add(commentData);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        commentData = commentsList.get(position);
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.message_thread_row, null);
        }
        profile_pic = (ImageView) view.findViewById(R.id.picture);
        firstText = (TextView) view.findViewById(R.id.name);
        firstText.setText(commentData.getFromUser().getName());
        secondText = (TextView) view.findViewById(R.id.message);
        secondText.setText(commentData.getMessage());
        createdTime = (TextView) view.findViewById(R.id.created_time);
        createdTime.setText(KaopotoUtil.formattedDateString(commentData.getCreatedTime()));
        String uid = commentData.getFromUser().getUid();
        String fromPic = activity.profilesDao.getImageUrl(uid);
        bmp = Utility.model.getImage(uid,fromPic);
        if(bmp != null){
            profile_pic.setImageBitmap(bmp);
        }else{
            profile_pic.setImageBitmap(null);
        }

        return view;
    }
}
