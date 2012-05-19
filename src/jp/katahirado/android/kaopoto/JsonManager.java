package jp.katahirado.android.kaopoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class JsonManager {
    public static JSONArray mJsonArray;

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

    public static JSONArray querySearchPages(String query){
        JSONArray resultArray = new JSONArray();
        for (int i=0;i< mJsonArray.length();i++){
            try {
                JSONObject object = mJsonArray.getJSONObject(i);
                String name = object.getString("name").toLowerCase();
                if (name.contains(query)) {
                    resultArray.put(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }
}
