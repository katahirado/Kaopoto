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
    private String name;
    private String caption;
    private String description;
    private String picture;

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
        try {
            name = jsonObject.getString(Const.NAME);
        } catch (JSONException e) {
            name="";
        }
        try{
            caption = jsonObject.getString("caption");
        }catch (JSONException e){
            caption ="";
        }
        try{
            description = jsonObject.getString("description");
        }catch (JSONException e){
            description ="";
        }
        try {
            picture = jsonObject.getString("picture");
        } catch (JSONException e) {
            picture = "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
