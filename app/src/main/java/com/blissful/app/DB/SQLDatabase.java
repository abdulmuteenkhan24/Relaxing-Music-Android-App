package com.blissful.app.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blissful.app.Activity.MainScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Blissful.db";
    public static final String Categories_TABLE_NAME = "Categories";
    public static final String Categories_COLUMN_ID = "id";
    public static final String Categories_COLUMN_NAME = "NAME";
    public static final String Categories_COLUMN_IMAGE = "IMAGE";
    public static final String Categories_COLUMN_VideosName = "VideosName";
    public static final String CONTACTS_COLUMN_VideosLink = "VideosLink";
    private HashMap hp;

    public SQLDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    public boolean isTableExists(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    public void CreateTable() {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(
                "create table Categories " +
                        "(id integer primary key, NAME text,IMAGE text,VideosName text, VideosLink text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Categories");
        onCreate(db);
    }

    public boolean insertCategories(String NAME, String IMAGE, String VideosName, String VideosLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", NAME);
        contentValues.put("IMAGE", IMAGE);
        contentValues.put("VideosName", VideosName);
        contentValues.put("VideosLink", VideosLink);
        db.insert("Categories", null, contentValues);
        new MainScreen().SetAdapter();
        return true;


    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Categories where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Categories_TABLE_NAME);
        return numRows;
    }

    public boolean updateCategories(Integer id, String NAME, String IMAGE, String VideosName, String VideosLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", NAME);
        contentValues.put("IMAGE", IMAGE);
        contentValues.put("VideosName", VideosName);
        contentValues.put("VideosLink", VideosLink);
        db.update("Categories", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteCategories(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Categories",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteAll(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Categories", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(1));
            res.moveToNext();
        }
        return array_list;
    }


}
