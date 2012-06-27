package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.KaopotoUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class EventData {
    private UserData owner;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private String location;
    private String privacy;
    private Date updatedTime;

    public EventData(JSONObject jsonObject) {
        try {
            owner = new UserData(jsonObject.getJSONObject("owner"));
        } catch (JSONException e) {
            owner = new UserData(new JSONObject());
        }
        try {
            name = jsonObject.getString(Const.NAME);
        } catch (JSONException e) {
            name = "";
        }
        try {
            description = jsonObject.getString(Const.DESCRIPTION);
        } catch (JSONException e) {
            description = "";
        }
        try {
            startTime = new Date(jsonObject.getLong("start_time") *
                    Const.MILLISECOND + Const.TIMEZONE_OFFSET);
        } catch (JSONException e) {
            startTime = null;
        }
        try {
            endTime = new Date(jsonObject.getLong("end_time") *
                    Const.MILLISECOND + Const.TIMEZONE_OFFSET);
        } catch (JSONException e) {
            endTime = null;
        }
        try {
            location = jsonObject.getString("location");
        } catch (JSONException e) {
            location = "";
        }
        try {
            privacy = jsonObject.getString("privacy");
        } catch (JSONException e) {
            privacy = "";
        }
        try {
            updatedTime = new Date(jsonObject.getLong(Const.UPDATED_TIME)
                    * Const.MILLISECOND);
        } catch (JSONException e) {
            updatedTime = null;
        }
    }

    public UserData getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartAndEnd() {
        return KaopotoUtil.formattedDateString(startTime) + " - " + KaopotoUtil.formattedTimeString(endTime);
    }

    public String getLocation() {
        return location;
    }

    public String getPrivacy() {
        String result = "";
        if (privacy.equals("OPEN")) {
            result = "公開イベント";
        } else if (privacy.equals("SECRET")) {
            result = "招待者のみのイベント";
        } else if (privacy.equals("CLOSED")) {
            result = privacy;
        }
        return result;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }
}
