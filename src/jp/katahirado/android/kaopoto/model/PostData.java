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
    private ArrayList<UserData> likes;
    private ArrayList<CommentData> comments;
    private int likesCount;
    private int commentsCount;

    public PostData(JSONObject jsonObject) {
        try {
            postId = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            postId = "";
        }
        try {
            fromUser = new UserData(jsonObject.getJSONObject(Const.FROM));
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
            message = jsonObject.getString(Const.MESSAGE);
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
        likes = new ArrayList<UserData>();
        try {
            likesCount= jsonObject.getJSONObject(Const.LIKES).getInt(Const.COUNT);
        } catch (JSONException e) {
            likesCount = 0;
        }
        try {
            JSONArray likesArray = jsonObject.getJSONObject(Const.LIKES).getJSONArray(Const.DATA);
            for(int i = 0;i<likesArray.length();i++){
                likes.add(new UserData(likesArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            commentsCount= jsonObject.getJSONObject(Const.COMMENTS).getInt(Const.COUNT);
        } catch (JSONException e) {
            commentsCount = 0;
        }
        comments = new ArrayList<CommentData>();
        try {
            JSONArray likesArray = jsonObject.getJSONObject(Const.COMMENTS).getJSONArray(Const.DATA);
            for(int i = 0;i<likesArray.length();i++){
                comments.add(new CommentData(likesArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public ArrayList<UserData> getLikes() {
        return likes;
    }

    public ArrayList<CommentData> getComments() {
        return comments;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }
}
