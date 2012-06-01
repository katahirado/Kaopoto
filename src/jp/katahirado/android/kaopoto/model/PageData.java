package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PageData {
    private String uid;
    private String name;
    private String picture;
    private String category;

    public PageData(JSONObject jsonObject) {
        try {
            uid = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            uid = "";
        }
        try {
            name = jsonObject.getString(Const.NAME);
        } catch (JSONException e) {
            name = "";
        }
        try {
            picture = jsonObject.getString(Const.PICTURE);
        } catch (JSONException e) {
            picture = "";
        }
        try {
            category = jsonObject.getString(Const.CATEGORY);
        } catch (JSONException e) {
            category = "";
        }
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getCategory() {
        return category;
    }
}
