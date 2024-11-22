package com.ravemaster.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper( Context context) {
        super(context, "Bookmarks.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IdTable(id INTEGER PRIMARY KEY AUTOINCREMENT,recipeId INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists IdTable");

    }

    public boolean insertData( int id){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recipeId",id);
        long result = database.insert("IdTable",null,contentValues);
        return result != -1;
    }

    public Cursor getIds(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("Select * from IdTable",null);
    }

}
