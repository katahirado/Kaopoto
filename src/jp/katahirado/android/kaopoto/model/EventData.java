package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
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
    private String startTime;
    private String endTime;
    private String location;
    private String privacy;
    private Date updatedTime;
    private String eventId;

    public EventData(JSONObject jsonObject) {
        try {
            eventId = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            eventId = "";
        }
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
            startTime = jsonObject.getString("start_time");
//            startTime = new Date(jsonObject.getLong("start_time") *
//                    Const.MILLISECOND + Const.TIMEZONE_OFFSET);
        } catch (JSONException e) {
            startTime = null;
        }
        try {
            endTime = jsonObject.getString("end_time");
//            endTime = new Date(jsonObject.getLong("end_time") *
//                    Const.MILLISECOND + Const.TIMEZONE_OFFSET);
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
//        return KaopotoUtil.formattedDateString(startTime) + " - "
// + KaopotoUtil.formattedTimeString(endTime);
        return startTime + "\n     - " + endTime;
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

    public String getEventId() {
        return eventId;
    }
}
