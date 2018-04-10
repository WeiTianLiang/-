package com.example.wtl.mynotes.Tool;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.List;

/**
 * 判断是否是第一次打开程序
 * Created by WTL on 2018/3/27.
 */

public class IsFirstOpen {
    
    public static void IsFirstOpen(int state,final FloatingActionButton button, final LinearLayout delete, ImageView handle_list, SharedPreferences preferences, Boolean user_first, Context context, SQLiteDatabase readbase, List<Notes> notesList, RecyclerView handle_recycler, Animation animation) {
        if(user_first){//第一次
            preferences.edit().putBoolean("FIRST", false).apply();
            ReadCuesor.ReadCuesor(state,button,delete,context,0,readbase,notesList,handle_recycler,animation);

            ContentValues cv = new ContentValues();//第一次加载排列方式
            cv.put(NotesDB.FORMAT,0);
            readbase.insert(NotesDB.FORMAT_NAME,null,cv);

            ContentValues cv1 = new ContentValues();//第一次加载字号
            cv1.put(NotesDB.WORD_SIZE,"ordinary");
            readbase.insert(NotesDB.WORD_NAME,null,cv1);

        }else {
            int x = 0;
            Cursor cursor = readbase.query(NotesDB.FORMAT_NAME,null,null,null,null,null,null);
            if(cursor.moveToLast()) {
                x = cursor.getInt(cursor.getColumnIndex("form"));
            }
            if(x == 1) {
                handle_list.setImageResource(R.mipmap.cardview);
            } else {
                handle_list.setImageResource(R.mipmap.listview);
            }
            ReadCuesor.ReadCuesor(state,button,delete,context,x,readbase,notesList,handle_recycler,animation);
        }
    }

    public static void IsFirstOpen( final LinearLayout delete, final ImageView abandon_dele, final ImageView abandon_move,ImageView handle_list, SharedPreferences preferences, Boolean user_first, Context context, SQLiteDatabase readbase, List<Notes> notesList, RecyclerView handle_recycler, Animation animation) {
        if(user_first){//第一次
            preferences.edit().putBoolean("FIRST", false).apply();
            ReadCuesor.ReadCuesor(delete,abandon_dele,abandon_move,context,0,readbase,notesList,handle_recycler,animation);
            ContentValues cv = new ContentValues();
            cv.put(NotesDB.FORMAT,0);
            readbase.insert(NotesDB.FORMAT_NAME,null,cv);
        }else {
            int x = 0;
            Cursor cursor = readbase.query(NotesDB.FORMAT_NAME,null,null,null,null,null,null);
            if(cursor.moveToLast()) {
                x = cursor.getInt(cursor.getColumnIndex("form"));
            }
            if(x == 1) {
                handle_list.setImageResource(R.mipmap.cardview);
            } else {
                handle_list.setImageResource(R.mipmap.listview);
            }
            ReadCuesor.ReadCuesor(delete,abandon_dele,abandon_move,context,x,readbase,notesList,handle_recycler,animation);
        }
    }

    public static void IsFirstOpenWord(TextView textView,SQLiteDatabase readbase) {
            String state = "";
            Cursor cursor = readbase.query(NotesDB.WORD_NAME,null,null,null,null,null,null);
            if(cursor.moveToLast()) {
                state = cursor.getString(cursor.getColumnIndex("size"));
            }
            if(state.equals("litter")) {
                textView.setText("小");
            }
            if(state.equals("ordinary")) {
                textView.setText("普通");
            }
            if(state.equals("large")) {
                textView.setText("大");
            }

    }
    
}
