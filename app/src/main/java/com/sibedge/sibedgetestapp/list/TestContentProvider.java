package com.sibedge.sibedgetestapp.list;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TestContentProvider extends android.content.ContentProvider {

    private SQLDatabaseHelper dbHelper;

    private static final int ALL_ITEMS = 1;
    private static final int SINGLE_ITEM = 2;

    private static final String AUTHORITY = "com.sibedge.contentprovider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/items");

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "items", ALL_ITEMS);
        uriMatcher.addURI(AUTHORITY, "items/#", SINGLE_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SQLDatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ITEMS:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(ItemsDB.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ItemsDB.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_ITEMS:
                break;
            case SINGLE_ITEM:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ItemsDB.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ITEMS:
                break;
            case SINGLE_ITEM:
                String id = uri.getPathSegments().get(1);
                selection = ItemsDB.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(ItemsDB.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ITEMS:
                break;
            case SINGLE_ITEM:
                String id = uri.getPathSegments().get(1);
                selection = ItemsDB.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(ItemsDB.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

}
