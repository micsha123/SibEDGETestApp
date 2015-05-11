package com.sibedge.sibedgetestapp.list;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemsDB {

    private static final String TAG = ItemsDB.class.getSimpleName();

    public static final String TABLE_NAME = "items_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CHECKBOX = "checkbox";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_CHECKBOX + " TEXT )";

        Log.d(TAG, "onCreate SQL: " + buildSQL);

        sqLiteDatabase.execSQL(buildSQL);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
        Log.d(TAG, "onUpgrade SQL: " + buildSQL);
        sqLiteDatabase.execSQL(buildSQL);
        onCreate(sqLiteDatabase);
    }

}