package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
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

        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.delete_floar);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.delete_down);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        final NotesAdapter adapter = new NotesAdapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);
        adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                delete.startAnimation(animation1);
                button.setVisibility(View.GONE);

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
                        delete.setVisibility(View.GONE);
                        delete.startAnimation(animation2);
                        button.setVisibility(View.VISIBLE);
                        adapter.isLongItem();
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
    }

    /*
    * 加载card布局
    * */
    public static void cardlist(final FloatingActionButton button, final LinearLayout delete, RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(manager);
        final Notes2Adapter adapter = new Notes2Adapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);
        adapter.setOnItemLongClickListener(new Notes2Adapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);

                adapter.setOnItemClickListener(new Notes2Adapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int x, boolean adro, List<Notes> list) {
                        for (int i = 0; i < list.size(); i++) {
                            notesList.add(list.get(i));
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
                        delete.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        adapter.isLongItem();
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
    }

    //运行使用lauoutanimal加载的动画
    private static void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_change_list_anim);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
