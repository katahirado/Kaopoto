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
import jp.katahirado.android.kaopoto.activity.CommentsListActivity;
import jp.katahirado.android.kaopoto.model.CommentData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentsListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ImageView image;
    private TextView name;
    private TextView message;
    private TextView likes;
    private ArrayList<CommentData> commentsList;
    private CommentsListActivity activity;

    public CommentsListAdapter(CommentsListActivity context, ArrayList<CommentData> comments) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        activity = context;
        Utility.model.setListener(this);
        commentsList = comments;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentsList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(CommentData commentData) {
        commentsList.add(commentData);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.comment_row, null);
        }

        CommentData commentData = commentsList.get(position);
        if (commentData != null) {
            String fromPic = activity.getImageURLFromDB(commentData.getFromUser().getUid());
            image = (ImageView) view.findViewById(R.id.comment_row_image);
            image.setImageBitmap(Utility.model.getImage(commentData.getCommentId(), fromPic));

            name = (TextView) view.findViewById(R.id.comment_row_name);
            name.setText(commentData.getFromUser().getName());
            message = (TextView) view.findViewById(R.id.comment_row_message);
            message.setText(commentData.getMessage());
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

    public void addAll(ArrayList<CommentData> comments) {
        commentsList = comments;
        this.notifyDataSetChanged();
    }
}
