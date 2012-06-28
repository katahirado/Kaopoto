package jp.katahirado.android.kaopoto;

import jp.katahirado.android.kaopoto.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class KaopotoUtil {

    private static final String FORMAT_DATE = "yyyy年MM月dd日";
    private static final String FORMAT_TIME = "HH:mm";

    public static String getProfileURL(String id) {
        return Const.FACEBOOK_MOBILE_URL + Const.PROF_URL + id;
    }

    public static String formattedDateString(Date date) {
        if (date != null) {
            return new SimpleDateFormat(FORMAT_DATE + " " + FORMAT_TIME).format(date);
        } else {
            return null;
        }
    }

    public static String formattedTimeString(Date date) {
        return new SimpleDateFormat(FORMAT_TIME).format(date);
    }

    public static String getMobileURL(String href) {
        return href.replace("http://www.facebook.com/", Const.FACEBOOK_MOBILE_URL);
    }

    public static ArrayList<ProfileData> parseFriends(String response) {
        ArrayList<ProfileData> resultList = new ArrayList<ProfileData>();
        try {
            JSONArray jArray = new JSONObject(response).getJSONArray(Const.DATA);
            for (int i = 0; i < jArray.length(); i++) {
                resultList.add(new ProfileData(jArray.getJSONObject(i), Const.PICTURE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<PageData> parsePageList(String response) {
        ArrayList<PageData> resultList = new ArrayList<PageData>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray(Const.DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                resultList.add(new PageData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<ProfileData> parseProfileList(JSONObject jsonObject) {
        ArrayList<ProfileData> resultList = new ArrayList<ProfileData>();
        try {
            JSONArray array = jsonObject.getJSONArray(Const.FQL_RESULT_SET);
            for (int i = 0; i < array.length(); i++) {
                resultList.add(new ProfileData(array.getJSONObject(i), Const.PIC_SQUARE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<NotificationData> parseNotifications(JSONObject jsonObject) {
        ArrayList<NotificationData> resultList = new ArrayList<NotificationData>();
        try {
            JSONArray array = jsonObject.getJSONArray(Const.FQL_RESULT_SET);
            for (int i = 0; i < array.length(); i++) {
                resultList.add(new NotificationData(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<CommentData> parseComments(String response) {
        ArrayList<CommentData> resultList;
        try {
            JSONArray cArray = new JSONObject(response).getJSONArray(Const.DATA);
            resultList = new ArrayList<CommentData>();
            for (int i = 0; i < cArray.length(); i++) {
                resultList.add(new CommentData(cArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            resultList = new ArrayList<CommentData>();
        }
        return resultList;
    }

    public static ArrayList<MessageThreadData> parseMessageThread(String response) {
        ArrayList<MessageThreadData> resultList = new ArrayList<MessageThreadData>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray(Const.DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                resultList.add(new MessageThreadData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
