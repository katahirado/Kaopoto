package jp.katahirado.android.kaopoto.model;

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

    public ProfileData(JSONObject jsonObject, String pictureName) {
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
            if (pictureName.equals(Const.PICTURE)) {
                picture = jsonObject.getString(Const.PICTURE);
            } else if (pictureName.equals(Const.PIC_SQUARE)) {
                picture = jsonObject.getString(Const.PIC_SQUARE);
            }
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
