package jp.katahirado.android.kaopoto;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class KaopotoUtil {

    public static String getProfileURL(String id) {
        return Const.FACEBOOK_MOBILE_URL+Const.PROF_URL + id;
    }

    public static int stringToTypeID(String object_type) {
        int result = 0;
        if(object_type.equals(Const.STREAM)){
            result= Const.STREAM_ID;
        }else if(object_type.equals(Const.EVENT)){
            result= Const.EVENT_ID;
        }
        return result;
    }
}
