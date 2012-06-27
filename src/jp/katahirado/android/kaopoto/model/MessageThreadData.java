package jp.katahirado.android.kaopoto.model;

import com.facebook.android.Utility;
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
    private String threadId;
    private UserData fromUser;
    private String message;

    public MessageThreadData(JSONObject object) {
        try {
            threadId = object.getString(Const.ID);
        } catch (JSONException e) {
            threadId = "";
        }
        toUsers = new ArrayList<UserData>();
        try {
            JSONArray toUserArray = object.getJSONObject(Const.TO).getJSONArray(Const.DATA);
            for (int i = 0; i < toUserArray.length(); i++) {
                toUsers.add(new UserData(toUserArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            fromUser = new UserData(object.getJSONObject(Const.FROM));
        } catch (JSONException e) {
            fromUser = new UserData(new JSONObject());
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
                    * Const.MILLISECOND);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            message = object.getString(Const.MESSAGE);
        } catch (JSONException e) {
            message = "";
        }
    }

    public String getThreadId() {
        return threadId;
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public CommentData getLastCommentData() {
        if (comments.size() > 0) {
            return comments.get(comments.size() - 1);
        } else {
            return null;
        }
    }

    public String getToUsersName() {
        String result = "";
        for (UserData user : toUsers) {
            if (!Utility.userUID.equals(user.getUid())) {
                result = result + " " + user.getName();
            }
        }
        return result;
    }

    public String getMessage() {
        return message;
    }
}
