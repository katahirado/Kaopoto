package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class MessageThreadData {
    private ArrayList<UserData> toUsers;
    private ArrayList<CommentData> comments;
    private Date updatedTime;

    public MessageThreadData(JSONObject object) {
        toUsers = new ArrayList<UserData>();
        try {
            JSONArray toUserArray = object.getJSONObject(Const.TO).getJSONArray(Const.DATA);
            for (int i = 0; i < toUserArray.length(); i++) {
                toUsers.add(new UserData(toUserArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        comments = new ArrayList<CommentData>();
        try {
            JSONArray cArray = object.getJSONObject(Const.COMMENTS).getJSONArray(Const.DATA);
            for (int i = 0; i < cArray.length(); i++) {
                comments.add(new CommentData(cArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            updatedTime = new Date(object.getLong(Const.UPDATED_TIME)
                    * Const.MILLISECOND + Const.TIMEZONE_OFFSET);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserData> getToUsers() {
        return toUsers;
    }

    public ArrayList<CommentData> getComments() {
        return comments;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }
}
