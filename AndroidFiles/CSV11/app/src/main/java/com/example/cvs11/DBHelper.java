package com.example.cvs11;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists mytable ("
                + "name text ,"
                + "price text ,"
                + "tag text ,"
                + "pid text );";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists mytable";
        onCreate(db);
        db.execSQL(sql);
    }
    public void insert(String name, String price, String tag, String pid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("price", price);
        value.put("tag", tag);
        value.put("tag", pid);
        db.insert("mytable", null, value);
        ///////////db.execSQL("INSERT INTO Person VALUES('" + name + "', " + price + ", '" + tag + "')");
        db.close();
    }
}