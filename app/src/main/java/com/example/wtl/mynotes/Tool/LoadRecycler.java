package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 加载布局
 * Created by WTL on 2018/3/27.
 */

public class LoadRecycler {

    /*
    * 加载list布局
    * */
    public static void loadlist(final FloatingActionButton button, final LinearLayout delete, RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具
        animation = AnimationUtils.loadAnimation(context, R.anim.change_list_anim_in);//初始化动画
        DefaultItemAnimator ain = new DefaultItemAnimator();
        ain.setRemoveDuration(300);
        recyclerView.setItemAnimator(ain);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        final NotesAdapter adapter = new NotesAdapter(list, context);
        recyclerView.setAdapter(adapter);
        recyclerView.startAnimation(animation);
        adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                return true;
            }
        });
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int x, boolean adro, List<Notes> list1) {
                for (int i = 0; i < list1.size(); i++) {
                    notesList.add(list1.get(i));
                }
                if (adro) {
                    stringList.add(x);//如果类型为true，则添加
                } else {
                    //否则，删除当前list中的选中值
                    for (int i = 0; i < stringList.size(); i++) {
                        if (stringList.get(i) == x) {
                            stringList.remove(i);
                        }
                    }
                }
                Collections.sort(stringList);//从小到大对list排序
            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < stringList.size(); i++) {
                    if (i == 0) adapter.removeNotes(stringList.get(i));
                    else adapter.removeNotes(stringList.get(i) - i);
                    //根据时间删除表中数据
                    database.delete(NotesDB.TABLE_NAME, NotesDB.TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                }
                stringList.removeAll(stringList);//清空表
                adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean OnItemLongClick() {
                        adapter.notifyDataSetChanged();
                        delete.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
            }
        });
    }

    /*
    * 加载card布局
    * */
    public static void cardlist(RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        animation = AnimationUtils.loadAnimation(context, R.anim.change_list_anim_in);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(manager);
        Notes2Adapter adapter = new Notes2Adapter(list, context);
        recyclerView.setAdapter(adapter);
        recyclerView.startAnimation(animation);
    }

}
