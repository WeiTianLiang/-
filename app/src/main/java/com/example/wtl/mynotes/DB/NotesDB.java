package com.example.wtl.mynotes.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 记事本数据库
 * Created by WTL on 2018/3/21.
 */

public class NotesDB extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "notes";//表名

    public static final String CONTENT = "content";//文字内容

    public static final String ID = "_id";//id

    public static final String TIME = "time";//创建时间

    public NotesDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
