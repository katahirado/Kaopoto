package jp.katahirado.android.kaopoto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import jp.katahirado.android.kaopoto.model.ProfileData;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class SQLiteManager {

    private static final String INSERT_PROFILES = "insert or replace into profiles (uid,name,picture) values (?,?,?);";
    private static final String SELECT_PROFILES_PICTURE = "select picture from profiles where uid = ?";
    private static final String SELECT_PROFILES_NAME = "select name from profiles where uid = ?";

    public static void setProfileData(SQLiteDatabase database) {
        ArrayList<ProfileData> aList = new ArrayList<ProfileData>();
        for (ProfileData data : JsonManager.getAList()) {
            aList.add(data);
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
                statement.bindString(2, object.getName());
                statement.bindString(3, object.getPicture());
                statement.executeInsert();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public static void setProfileDatum(SQLiteDatabase database, ProfileData profileData) {
        SQLiteStatement statement = database.compileStatement(INSERT_PROFILES);
        statement.bindString(1, profileData.getUid());
        statement.bindString(2, profileData.getName());
        statement.bindString(3, profileData.getPicture());
        statement.executeInsert();
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

    public static String getUserName(SQLiteDatabase database, String uid) {
        String result = "";
        Cursor cursor = database.rawQuery(SELECT_PROFILES_NAME, new String[]{uid});
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Const.NAME));
        }
        cursor.close();
        return result;
    }
}
