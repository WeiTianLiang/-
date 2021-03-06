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

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库读取值
 * Created by WTL on 2018/3/27.
 */

public class ReadCuesor {

    /*
    * 从数据库获取颜色并返回
    * */
    public static List<String> ReadColor(SQLiteDatabase readbase) {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        List<String> color = new ArrayList<>();
        if(cursor.moveToLast()) {
            do {
                color.add(cursor.getString(cursor.getColumnIndex("color")));
            } while (cursor.moveToPrevious());
        }
        return color;
    }
    /*
    * 搜索并加载
    * */
    public static void SearchFromSQL(String search,Context context,SQLiteDatabase readbase,List<Notes> notesList,RecyclerView notes_list) {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        List<String> color = new ArrayList<>();
        List<Notes> notesList1 = new ArrayList<>();
        int x = search.length();
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                color.add(cursor.getString(cursor.getColumnIndex("color")));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
        }
        //搜索想要查找的值
        for(int i = 0 ; i < notesList.size() ; i++) {
            for(int j = 0 ; j <= notesList.get(i).getNotes_content_part().length()-x ; j++) {
                if(search.equals(notesList.get(i).getNotes_content_part().substring(j,j+x))) {
                    notesList1.add(notesList.get(i));
                    break;
                }
            }
        }
        LoadRecycler.loadlist(null,null,null,color,0,null,null,notes_list,null,context,notesList1);
    }
    /*
    * 从数据库获取值并加载到正常的recyclerview上
    * */
    public static void ReadCuesor(int state,final FloatingActionButton button, final LinearLayout delete, Context context, int x, SQLiteDatabase readbase, List<Notes> notesList, RecyclerView notes_list, Animation change_list_in) {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        List<String> color = new ArrayList<>();
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                color.add(cursor.getString(cursor.getColumnIndex("color")));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
        }
        if(x == 0) LoadRecycler.loadlist(null,null,null,color,state,button,delete,notes_list,change_list_in,context,notesList);
        else LoadRecycler.cardlist(null,null,null,color,state,button,delete,notes_list,change_list_in,context,notesList);
    }
    /*
    * 从数据库获取值并加载到垃圾箱的recyclerview上
    * */
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
