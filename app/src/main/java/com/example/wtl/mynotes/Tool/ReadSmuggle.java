package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;

import java.util.List;

/**
 * 加载Smuggle数据库的布局
 * Created by WTL on 2018/4/26.
 */

public class ReadSmuggle {

    /*
    * 从数据库获取值并加载到recyclerview上
    * */
    public static void ReadSmuggle(Context context, List<Sumggle> list, RecyclerView smuggle_list) {
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具

        Cursor cursor = database.query(NotesDB.NOTECLIP_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
            do{
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                Sumggle sumggle = new Sumggle(memo,"0");
                list.add(sumggle);
            }while (cursor.moveToPrevious());
        }
        LoadSmuggle.loadSmuggle(null,null,smuggle_list,context,list);
    }

}
