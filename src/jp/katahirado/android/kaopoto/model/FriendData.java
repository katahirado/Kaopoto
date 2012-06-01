package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class FriendData {
    private String uid;
    private String name;
    private String picture;
    private String birthday;

    public FriendData(JSONObject jsonObject) {
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
            birthday = jsonObject.getString(Const.BIRTHDAY);
        } catch (JSONException e) {
            birthday = "";
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

    public String getBirthday() {
        return birthday;
    }
}
