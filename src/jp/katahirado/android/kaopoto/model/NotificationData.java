package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class NotificationData {
    private String senderId;
    private String titleText;
    private String objectType;
    private String objectId;
    private String href;

    public NotificationData(JSONObject jsonObject) {
        try {
            senderId = jsonObject.getString(Const.SENDER_ID);
        } catch (JSONException e) {
            senderId = "";
        }
        try {
            objectId = jsonObject.getString(Const.OBJECT_ID);
        } catch (JSONException e) {
            objectId = "";
        }
        try {
            objectType = jsonObject.getString("object_type");
        } catch (JSONException e) {
            objectType = "";
        }
        try {
            titleText = jsonObject.getString("title_text");
        } catch (JSONException e) {
            titleText = "";
        }
        try {
            href = KaopotoUtil.getMobileURL(jsonObject.getString("href"));
        } catch (JSONException e) {
            href = "";
        }
    }

    public String getSenderId() {
        return senderId;
    }

    public CharSequence getTitleText() {
        return titleText;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getHref() {
        return href;
    }
}
