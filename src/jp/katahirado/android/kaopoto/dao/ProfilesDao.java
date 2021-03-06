package jp.katahirado.android.kaopoto.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.model.ProfileData;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class ProfilesDao {

    private static final String TABLE_NAME= "profiles";
    private static final String INSERT_PROFILES = "insert or replace into "+TABLE_NAME+
            " (uid,name,picture) values (?,?,?);";
    private static final String SELECT_PROFILES_PICTURE = "select picture from "+TABLE_NAME+" where uid = ?";
    private static final String SELECT_PROFILES_NAME = "select name from "+TABLE_NAME+" where uid = ?";

    private SQLiteDatabase database;

    public ProfilesDao(SQLiteDatabase database) {
        this.database = database;
    }

    public void insert(ProfileData profileData) {
        SQLiteStatement statement = database.compileStatement(INSERT_PROFILES);
        statement.bindString(1, profileData.getUid());
        statement.bindString(2, profileData.getName());
        statement.bindString(3, profileData.getPicture());
        statement.executeInsert();
    }

    public String getImageUrl(String uid) {
        String result = "";
        Cursor cursor = database.rawQuery(SELECT_PROFILES_PICTURE, new String[]{uid});
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Const.PICTURE));
        }
        cursor.close();
        return result;
    }

    public String getUserName(String uid) {
        String result = "";
        Cursor cursor = database.rawQuery(SELECT_PROFILES_NAME, new String[]{uid});
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Const.NAME));
        }
        cursor.close();
        return result;
    }

    public void bulkInsert(ArrayList<ProfileData> profileLists) {
        database.beginTransaction();
        try {
            SQLiteStatement statement = database.compileStatement(INSERT_PROFILES);
            for (ProfileData object : profileLists) {
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

    public void vacuum() {
        try{
            database.execSQL("VACUUM "+TABLE_NAME);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
