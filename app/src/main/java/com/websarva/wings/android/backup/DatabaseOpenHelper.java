package com.websarva.wings.android.backup;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static String DATABASE_NAME = HomeActivity.DATABASE_NAME;
    private static final String TABLE_NAME = "foodinfo";
    private static final String TABLE_NAME2 = "userinfo";
    private static final String _ID = "_id";
    private static final String _ID2 = "_id";
    private static final String FOOD_NAME = "foodName";
    private static final String FOOD_BUY_DATE = "buyDate";
    private static final String FOOD_DUE_DATE = "dueDate";
    private static final String FOOD_PRICE = "foodPrice";
    private static final String USER_UPPER_LIMIT_PRICE = "upperLimitPrice";
    private static final String USER_NOTIFICATION_TIMING = "userNotificationTiming";
    private static final String USER_LANGUAGE = "userLanguage";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    FOOD_NAME + " TEXT, " +
                    FOOD_BUY_DATE + " INTEGER, " +
                    FOOD_DUE_DATE + " INTEGER, " +
                    FOOD_PRICE + " INTEGER)";

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + TABLE_NAME2 + " (" +
                    _ID2 + " INTEGER PRIMARY KEY," +
                    USER_UPPER_LIMIT_PRICE + " INTEGER, " +
                    USER_NOTIFICATION_TIMING + " INTEGER, " +
                    USER_LANGUAGE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + TABLE_NAME2;


    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);

        ContentValues values = new ContentValues();
        values.put("upperLimitPrice", 30000);
        values.put("userNotificationTiming", 1);
        values.put("userLanguage", 1);

        db.insert("userinfo", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        db.execSQL(
                SQL_DELETE_ENTRIES2
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}