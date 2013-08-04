package com.zion.htf;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Item extends ContentProvider{
    public static final String TAG = "Item";
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SECTION = 1;

    protected String name;
    protected int type;
    protected DatabaseOpenHelper database;

    public static final String PROVIDER_NAME = "com.zion.htf.sets";

    /** A uri to do operations on sets table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/sets" );

    /** Constants to identify the requested operation */
    private static final int SETS = 1;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "sets", SETS);
    }

    public Item(){
        super();
    }

    public Item(String name){
        this.name = name;
        this.type = Item.TYPE_SECTION;
    }

    public Item(String name, int type){
        this.name = name;
        this.type = type;
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return this.name;
    }

    /** A callback method which is by the default content uri */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(true || uriMatcher.match(uri) == SETS){
            String sql =    "SELECT " +
                                "sets.id AS _id, " +
                                "artists.name AS artist, " +
                                "lst__genres.label AS genre, " +
                                "begin_date, " +
                                "end_date, " +
                                "lst__set_types.label " +
                            "FROM sets " +
                                "JOIN artists on sets.artist = artists.id " +
                                "JOIN lst__genres ON artists.genre = lst__genres.id " +
                                "JOIN lst__set_types ON sets.type = lst__set_types.id;";
            return this.database.getReadableDatabase().rawQuery(sql, null);
        }
        else{
            return null;
        }
    }

    @Override
    public boolean onCreate() {
        this.database = new DatabaseOpenHelper(this.getContext(), "database.sqlite", null);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // We don't delete
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // We don't insert
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // We don't update
        return 0;
    }
}
