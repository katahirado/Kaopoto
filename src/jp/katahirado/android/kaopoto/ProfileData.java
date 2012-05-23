package jp.katahirado.android.kaopoto;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class ProfileData {
    private String uid;
    private String picture;

    public ProfileData(String id, String pic) {
        uid = id;
        picture = pic;
    }

    public String getUid() {
        return uid;
    }

    public String getPicture() {
        return picture;
    }
}
