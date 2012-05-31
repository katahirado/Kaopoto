package jp.katahirado.android.kaopoto.dao;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class ProfileData {
    private String uid;
    private String picture;
    private String name;

    public ProfileData(String id,String n, String pic) {
        uid = id;
        name =n;
        picture = pic;
    }


    public String getUid() {
        return uid;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }
}
