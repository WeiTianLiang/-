package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;

import java.util.List;

/**
 * 从数据库读取值
 * Created by WTL on 2018/3/27.
 */

public class ReadCuesor {

    public static void ReadCuesor(int state,final FloatingActionButton button, final LinearLayout delete, Context context, int x, SQLiteDatabase readbase, List<Notes> notesList, RecyclerView notes_list, Animation change_list_in) {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
        }
        if(x == 0) LoadRecycler.loadlist(state,button,delete,notes_list,change_list_in,context,notesList);
        else LoadRecycler.cardlist(state,button,delete,notes_list,change_list_in,context,notesList);
    }

    public static void ReadCuesor(final LinearLayout delete, final ImageView abandon_dele, final ImageView abandon_move, Context context, int x, SQLiteDatabase readbase, List<Notes> notesList, RecyclerView notes_list, Animation change_list_in) {
        Cursor cursor = readbase.query(NotesDB.DELETE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("delete_content"));
                String time = cursor.getString(cursor.getColumnIndex("delete_time"));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
        }
        if(x == 0) LoadRecycler.loadlist(delete,abandon_dele,abandon_move,notes_list,change_list_in,context,notesList);
        else LoadRecycler.cardlist(delete,abandon_dele,abandon_move,notes_list,change_list_in,context,notesList);
    }

}
