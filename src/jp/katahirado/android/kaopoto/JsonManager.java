package jp.katahirado.android.kaopoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class JsonManager {
    public static JSONArray mJsonArray;
    private static ArrayList<ProfileData> aList;

    public static void setAList(ArrayList<ProfileData> array){
        aList = array;
    }

    public static ArrayList<ProfileData> getAList() {
        return aList;
    }

    public static JSONArray querySearchFriends(String query) {
        JSONArray resultArray = new JSONArray();
        String key;
        if (query.matches("^[0-9/]*")) {
            key = Const.BIRTHDAY;
        } else {
            key = Const.NAME;
        }
        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject object = mJsonArray.getJSONObject(i);
                String name = object.getString(key).toLowerCase();
                if (name.contains(query)) {
                    resultArray.put(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    public static JSONArray querySearchPages(String query) {
        JSONArray resultArray = new JSONArray();
        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject object = mJsonArray.getJSONObject(i);
                String name = object.getString(Const.NAME).toLowerCase();
                if (name.contains(query)) {
                    resultArray.put(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    public static JSONObject getImageObject(String senderId) {
        JSONObject resultObject = new JSONObject();
        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject object = mJsonArray.getJSONObject(i);
                String id = object.getString(Const.ID);
                if (id.equals(senderId)) {
                    resultObject = object;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultObject;
    }

    public static void setAList(String pictureName) {
        JSONObject object;
        String picture = "";
        String uid;
        ArrayList<ProfileData> arrayList= new ArrayList<ProfileData>();
        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                object = mJsonArray.getJSONObject(i);
                uid = object.getString(Const.ID);
                if (pictureName.equals(Const.PIC_SQUARE)) {
                    picture = object.getString(Const.PIC_SQUARE);
                } else if (pictureName.equals(Const.PICTURE)) {
                    picture = object.getString(Const.PICTURE);
                }
                arrayList.add(new ProfileData(uid, picture));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setAList(arrayList);
        }
    }
}
