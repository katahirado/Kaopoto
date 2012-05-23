package jp.katahirado.android.kaopoto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class UserData {
    private String name;
    private String uid;

    public UserData(JSONObject object) {
        try {
            uid = object.getString(Const.ID);
            name = object.getString(Const.NAME);
        } catch (JSONException e) {
            e.printStackTrace();
            uid="";
            name="";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
