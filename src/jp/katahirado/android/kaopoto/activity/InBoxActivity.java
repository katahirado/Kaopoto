package jp.katahirado.android.kaopoto.activity;

import android.app.ListActivity;
import android.os.Bundle;
import jp.katahirado.android.kaopoto.R;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class InBoxActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_box);
    }
}