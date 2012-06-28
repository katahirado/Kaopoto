package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.CommentsListActivity;
import jp.katahirado.android.kaopoto.model.CommentData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentsListAdapter extends FacebookBaseAdapter {
    private TextView likes;
    private ArrayList<CommentData> commentsList;
    private CommentsListActivity activity;
    private CommentData commentData;

    public CommentsListAdapter(CommentsListActivity context, ArrayList<CommentData> comments) {
        super(context);
        activity = context;
        commentsList = comments;
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

    public void addAll(ArrayList<CommentData> comments) {
        commentsList = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.comment_row, null);
        }

        commentData = commentsList.get(position);
        if (commentData != null) {
            String fromPic = activity.profilesDao.getImageUrl(commentData.getFromUser().getUid());
            profile_pic = (ImageView) view.findViewById(R.id.comment_row_image);
            profile_pic.setImageBitmap(Utility.model.getImage(commentData.getCommentId(), fromPic));

            firstText = (TextView) view.findViewById(R.id.comment_row_name);
            firstText.setText(commentData.getFromUser().getName());
            secondText = (TextView) view.findViewById(R.id.comment_row_message);
            secondText.setText(commentData.getMessage());
            likes = (TextView) view.findViewById(R.id.comment_row_likes);
            if (commentData.getLikes() == 0) {
                likes.setVisibility(View.GONE);
            } else {
                likes.setVisibility(View.VISIBLE);
                likes.setText(commentData.getLikes() + activity.getString(R.string.people_likes));
            }
        }
        return view;
    }
}
