package jp.katahirado.android.kaopoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.model.CommentData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentsListAdapter extends ArrayAdapter<CommentData> {
    private LayoutInflater layoutInflater;
    private TextView name;
    private TextView message;

    public CommentsListAdapter(Context context, ArrayList<CommentData> comments) {
        super(context, 0, comments);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.comment_row,null);
        }

        CommentData commentData = this.getItem(position);
        if (commentData != null) {
            name = (TextView) view.findViewById(R.id.comment_row_name);
            name.setText(commentData.getFromUser().getName());
            message = (TextView) view.findViewById(R.id.comment_row_message);
            message.setText(commentData.getMessage());
//            screenName = (TextView) view.findViewById(R.id.row_screen_name);
//            screenName.setText(status.getUser().getScreenName());
//            createdAt = (TextView) view.findViewById(R.id.row_created_at);
//            formatDateText = new SimpleDateFormat("yyyy年MM月dd日HH時mm分").format(status.getCreatedAt());
//            createdAt.setText(" " + formatDateText);
//            tweetText = (TextView) view.findViewById(R.id.row_tweet_text);
//            tweetText.setText(status.getText());
        }
        return view;
    }
}
