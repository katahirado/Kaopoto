package jp.katahirado.android.kaopoto.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import jp.katahirado.android.kaopoto.Const;
import jp.katahirado.android.kaopoto.JsonManager;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class ProfilesDao {

    private static final String INSERT_PROFILES = "insert or replace into profiles (uid,name,picture) values (?,?,?);";
    private static final String SELECT_PROFILES_PICTURE = "select picture from profiles where uid = ?";
    private static final String SELECT_PROFILES_NAME = "select name from profiles where uid = ?";

    private SQLiteDatabase database;

    public ProfilesDao(SQLiteDatabase database) {
        this.database = database;
    }

    public void bulkInsert() {
        ArrayList<ProfileData> aList = new ArrayList<ProfileData>();
        for (ProfileData data : JsonManager.getAList()) {
            aList.add(data);
        }
        if (aList.size() > 0) {
            internalBulkInsert(aList);
        }
    }

    private void internalBulkInsert(ArrayList<ProfileData> aList) {
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
}
