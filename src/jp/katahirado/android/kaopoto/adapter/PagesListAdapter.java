package jp.katahirado.android.kaopoto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Utility;
import jp.katahirado.android.kaopoto.R;
import jp.katahirado.android.kaopoto.activity.PagesListActivity;
import jp.katahirado.android.kaopoto.model.PageData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PagesListAdapter extends FacebookBaseAdapter {

    private ArrayList<PageData> pageList;
    private PageData pageData;

    public PagesListAdapter(PagesListActivity context, ArrayList<PageData> pageList) {
        super(context);
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
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
