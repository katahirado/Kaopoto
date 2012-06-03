package jp.katahirado.android.kaopoto.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * Author: yuichi_katahira
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String CREATE_PROFILE_TABLE = "create table profiles (" +
            " _id integer primary key autoincrement, uid text not null," +
            " name text not null, picture text not null)";
    private static final String CREATE_PROFILE_INDEX = "create unique index uidUnique on profiles (uid);";


    public DBOpenHelper(Context context) {
        super(context, "kaopoto.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROFILE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
