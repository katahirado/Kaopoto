package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PostData {
    private UserData fromUser;
    private String postId;
    private String message;
    private ArrayList<UserData> toUsers;

    public PostData(JSONObject jsonObject) {
        try {
            postId = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            postId="";
        }
        try {
            fromUser= new UserData(jsonObject.getJSONObject("from"));
        } catch (JSONException e) {
            fromUser =new UserData(new JSONObject());
        }
        toUsers = new ArrayList<UserData>();
        try {
            JSONArray toUserArray=jsonObject.getJSONObject("to").getJSONArray(Const.DATA);
            for (int i = 0; i < toUserArray.length(); i++) {
                toUsers.add(new UserData(toUserArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            message="";
        }
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserData fromUser) {
        this.fromUser = fromUser;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserData> getToUsers() {
        return toUsers;
    }

    public void setToUsers(ArrayList<UserData> toUsers) {
        this.toUsers = toUsers;
    }
}
