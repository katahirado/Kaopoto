package jp.katahirado.android.kaopoto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class SQLiteManager {

    private static final String INSERT_PROFILES = "insert into profiles (uid,picture) values (?,?);";
    private static final String SELECT_PROFILES_UID = "select uid from profiles where uid = ?";
    private static final String SELECT_PROFILES_PICTURE = "select picture from profiles where uid = ?";

    public static void setProfileData(SQLiteDatabase database) {
        ArrayList<ProfileData> aList = new ArrayList<ProfileData>();
        for (ProfileData data : JsonManager.getAList()) {
            Cursor cursor = database.rawQuery(SELECT_PROFILES_UID, new String[]{data.getUid()});
            if (!cursor.moveToFirst()) {
                aList.add(data);
            }
            cursor.close();
        }
        if (aList.size() > 0) {
            setProfileDatum(database, aList);
        }
    }

    private static void setProfileDatum(SQLiteDatabase database, ArrayList<ProfileData> aList) {
        database.beginTransaction();
        try {
            SQLiteStatement statement = database.compileStatement(INSERT_PROFILES);
            for (ProfileData object : aList) {
                statement.bindString(1, object.getUid());
                statement.bindString(2, object.getPicture());
                statement.executeInsert();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public static void setProfileDatum(SQLiteDatabase database, ProfileData profileData) {
        Cursor cursor = database.rawQuery(SELECT_PROFILES_UID, new String[]{profileData.getUid()});
        if (!cursor.moveToFirst()) {
            SQLiteStatement statement = database.compileStatement(INSERT_PROFILES);
            statement.bindString(1, profileData.getUid());
            statement.bindString(2, profileData.getPicture());
            statement.executeInsert();
        }
        cursor.close();
    }

    public static String getImageUrl(SQLiteDatabase database, String uid) {
        String result = "";
        Cursor cursor = database.rawQuery(SELECT_PROFILES_PICTURE, new String[]{uid});
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Const.PICTURE));
        }
        cursor.close();
        return result;
    }
}
