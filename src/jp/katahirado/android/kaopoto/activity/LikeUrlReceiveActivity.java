package jp.katahirado.android.kaopoto.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import jp.katahirado.android.kaopoto.R;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class LikeUrlReceiveActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_url_receive);

        WebView likeBox = (WebView) findViewById(R.id.like_box_webview);
        WebSettings settings = likeBox.getSettings();
        settings.setJavaScriptEnabled(true);


        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            String url = intent.getExtras().getCharSequence(Intent.EXTRA_TEXT).toString();
            if (url != null) {
                likeBox.loadData(buildLikeBoxURLString(url), "text/html", "UTF-8");
            }
        }
    }

    private String buildLikeBoxURLString(String url) {
        String iFrame = "<iframe src=\"http://www.facebook.com/plugins/like.php?app_id=id=204927256201280&amp;" +
                "href=" + url + "&amp;send=false&amp;layout=standard&amp;width=400&amp;show_faces=true&amp;" +
                "action=like&amp;colorscheme=light&amp;font&amp;height=80\" scrolling=\"no\" frameborder=\"0\"" +
                " style=\"border:none; overflow:hidden; width:400px; height:80px;\" allowTransparency=\"true\">" +
                "</iframe><iframe src=\"" + url + "\"></iframe>";
        return buildHTMLBody(iFrame);
    }

    private String buildHTMLBody(String iFrame) {
        return "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                "<style type=\"text/css\">body { font-size: small; color: #CCCCCC ; }</style><body>" + iFrame +
                "</body></html>";
    }
}