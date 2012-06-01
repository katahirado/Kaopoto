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
import jp.katahirado.android.kaopoto.activity.PagesListActivity;
import jp.katahirado.android.kaopoto.model.PageData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PagesListAdapter extends BaseAdapter {

    private ArrayList<PageData> pageList;
    private LayoutInflater layoutInflater;
    private ImageView profile_pic;
    private TextView firstText;
    private TextView secondText;
    private PageData pageData;

    public PagesListAdapter(PagesListActivity context, ArrayList<PageData> pageList) {
        if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        this.pageList = pageList;
        Utility.model.setListener(this);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        pageData = pageList.get(position);
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.page_row, null);
        }

        profile_pic = (ImageView) view.findViewById(R.id.page_profile_pic);
        firstText = (TextView) view.findViewById(R.id.page_name);
        secondText = (TextView) view.findViewById(R.id.page_category);
        profile_pic.setImageBitmap(Utility.model.getImage(pageData.getUid(), pageData.getPicture()));
        firstText.setText(pageData.getName());
        secondText.setText(pageData.getCategory());
        return view;
    }
}
