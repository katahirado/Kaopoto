package jp.katahirado.android.kaopoto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class PostData {
    private UserData fromData;
    private String postId;
    private String message;

    public PostData(JSONObject jsonObject) throws JSONException{
        String pId = jsonObject.getString(Const.ID);
        if(pId!=null){
            postId = pId;
        }
        JSONObject fObject = jsonObject.getJSONObject("from");
        if(fObject!=null){
            fromData = new UserData(fObject);
        }
        String mess = jsonObject.getString("message");
        if(mess!=null){
           message = mess;
        }
    }

    public UserData getFromData() {
        return fromData;
    }

    public void setFromData(UserData fromData) {
        this.fromData = fromData;
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
}
