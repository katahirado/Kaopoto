package jp.katahirado.android.kaopoto.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.model.MessageThreadData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class InBoxAdapter extends FacebookBaseAdapter {

    private ArrayList<MessageThreadData> messageThreadList;

    public InBoxAdapter(Activity context, ArrayList<MessageThreadData> list) {
        super(context);
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
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.in_box_row, null);
        }
        return view;
    }
}
