package com.example.wtl.mynotes.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 记事本数据库
 * Created by WTL on 2018/3/21.
 */

public class NotesDB extends SQLiteOpenHelper{
    //表1
    public static final String TABLE_NAME = "notes";//表名
    public static final String CONTENT = "content";//文字内容
    public static final String ID = "_id";//id
    public static final String TIME = "time";//创建时间
    //表2
    public static final String FORMAT_NAME = "format";//表名
    public static final String FORMAT = "form";//创建排版格式
    public static final String FORMAT_ID = "_id";
    //表3
    public static final String NOTECLIP_NAME = "noteclip";//表名
    public static final String NOTECLIP_ID = "_id";
    public static final String NOTECLIP_MEMO = "memo";//文件夹名称
    public static final String NOTECLIP_NUMBER = "number";//拥有文件数量

    public NotesDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + FORMAT_NAME + "("
                + FORMAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FORMAT +" INTEGER NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + NOTECLIP_NAME + "("
                + NOTECLIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTECLIP_MEMO + " TEXT NOT NULL,"
                + NOTECLIP_NUMBER + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
