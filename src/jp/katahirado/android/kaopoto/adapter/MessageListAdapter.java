package jp.katahirado.android.kaopoto.adapter;

import jp.katahirado.android.kaopoto.activity.MessageThreadActivity;
import jp.katahirado.android.kaopoto.model.CommentData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageListAdapter extends FacebookBaseAdapter {
    public MessageListAdapter(MessageThreadActivity activity, ArrayList<CommentData> lists) {
        super(activity);
    }
}
