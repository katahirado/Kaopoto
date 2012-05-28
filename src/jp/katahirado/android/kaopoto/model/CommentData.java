package jp.katahirado.android.kaopoto.model;

import jp.katahirado.android.kaopoto.Const;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class CommentData {
    private String commentId;
    private UserData fromUser;
    private String message;
    private int likes;

    public CommentData(JSONObject jsonObject) {
        try {
            commentId = jsonObject.getString(Const.ID);
        } catch (JSONException e) {
            commentId = "";
        }
        try {
            fromUser = new UserData(jsonObject.getJSONObject(Const.FROM));
        } catch (JSONException e) {
            fromUser = new UserData(new JSONObject());
        }
        try {
            message = jsonObject.getString(Const.MESSAGE);
        } catch (JSONException e) {
            message = "";
        }
        try {
            likes = jsonObject.getInt(Const.LIKES);
        } catch (JSONException e) {
            likes = 0;
        }
    }

    public CommentData() {
    }

    public String getCommentId() {
        return commentId;
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public String getMessage() {
        return message;
    }

    public int getLikes() {
        return likes;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setFromUser(UserData fromUser) {
        this.fromUser = fromUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public static CommentData buildCommentData(String response, String message, UserData userData) {
        CommentData commentData = new CommentData();
        try {
            commentData.commentId = new JSONObject(response).getString(Const.ID);
        } catch (JSONException e) {
            commentData.commentId = "";
        }
        commentData.setMessage(message);
        commentData.setLikes(0);
        commentData.setFromUser(userData);
        return commentData;
    }
}
