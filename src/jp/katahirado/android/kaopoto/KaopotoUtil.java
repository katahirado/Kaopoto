package jp.katahirado.android.kaopoto;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class KaopotoUtil {

    public static String getProfileURL(String id) {
        return Const.FACEBOOK_MOBILE_URL+Const.PROF_URL + id;
    }

}
