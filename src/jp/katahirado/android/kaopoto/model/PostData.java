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
    private String source;
    private String link;

    public PostData(JSONObject jsonObject) {
        try {
            postId = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            postId = "";
        }
        try {
            fromUser = new UserData(jsonObject.getJSONObject("from"));
        } catch (JSONException e) {
            fromUser = new UserData(new JSONObject());
        }
        toUsers = new ArrayList<UserData>();
        try {
            JSONArray toUserArray = jsonObject.getJSONObject(Const.TO).getJSONArray(Const.DATA);
            for (int i = 0; i < toUserArray.length(); i++) {
                toUsers.add(new UserData(toUserArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            message = "";
        }
        try {
            link = jsonObject.getString("link");
        } catch (JSONException e) {
            link = "";
        }
        try {
            name = jsonObject.getString(Const.NAME);
        } catch (JSONException e) {
            name = "";
        }
        try {
            caption = jsonObject.getString(Const.CAPTION);
        } catch (JSONException e) {
            caption = "";
        }
        try {
            description = jsonObject.getString(Const.DESCRIPTION);
        } catch (JSONException e) {
            description = "";
        }
        try {
            picture = jsonObject.getString(Const.PICTURE);
        } catch (JSONException e) {
            picture = "";
        }
        try {
            source = jsonObject.getString("source");
        } catch (JSONException e) {
            source = "";
        }
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public String getPostId() {
        return postId;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<UserData> getToUsers() {
        return toUsers;
    }

    public String getName() {
        return name;
    }


    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getSource() {
        return source;
    }

    public String getLink() {
        return link;
    }

}
