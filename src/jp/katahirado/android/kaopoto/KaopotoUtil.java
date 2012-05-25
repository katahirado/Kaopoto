package jp.katahirado.android.kaopoto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class KaopotoUtil {

    private static final String FORMAT_DATE = "yyyy年MM月dd日";
    private static final String FORMAT_TIME = "HH:mm";

    public static String getProfileURL(String id) {
        return Const.FACEBOOK_MOBILE_URL + Const.PROF_URL + id;
    }

    public static int stringToTypeID(String object_type) {
        int result = 0;
        if (object_type.equals(Const.STREAM)) {
            result = Const.STREAM_ID;
        } else if (object_type.equals(Const.EVENT)) {
            result = Const.EVENT_ID;
        }
        return result;
    }

    public static String formattedDateString(Date date) {
        return new SimpleDateFormat(FORMAT_DATE + " " + FORMAT_TIME).format(date);
    }

    public static String formattedTimeString(Date date) {
        return new SimpleDateFormat(FORMAT_TIME).format(date);
    }
}
