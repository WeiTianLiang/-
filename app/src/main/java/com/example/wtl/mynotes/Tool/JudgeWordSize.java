package com.example.wtl.mynotes.Tool;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wtl.mynotes.DB.NotesDB;

/**
 * 判断字号大小
 * Created by WTL on 2018/4/11.
 */

public class JudgeWordSize {

    public static String JudgeWordSize(SQLiteDatabase database) {
        String state = "";
        Cursor cursor = database.query(NotesDB.WORD_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
            state = cursor.getString(cursor.getColumnIndex("size"));
        }
        return state;
    }

}
