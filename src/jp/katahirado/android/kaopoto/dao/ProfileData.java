package jp.katahirado.android.kaopoto.dao;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class ProfileData {
    private String uid;
    private String name;
    private String picture;
    private String birthday;

    public ProfileData(String id,String n, String pic) {
        uid = id;
        name =n;
        picture = pic;
    }

    public ProfileData(JSONObject jsonObject) {
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

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }
}
