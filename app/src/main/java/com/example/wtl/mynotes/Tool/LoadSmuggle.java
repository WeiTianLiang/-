package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import com.example.wtl.mynotes.Adapter.SumggleAdapter;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;

import java.util.List;

/**
 * Smuggle增删改类
 * 与LoadRecycler基本类似，当初在设计LoadRecycler时没有考虑到会在这里用到，
 * 而现在再去重写LoadRecycler感觉有点得不偿失于是重新写了这个LoadSmuggle
 * <p>
 * Created by WTL on 2018/4/26.
 */

public class LoadSmuggle {

    /*
    * 加载Smuggle布局
    * */
    public static void loadSmuggle(String str,Sumggle sum,final RecyclerView recyclerView, final Context context, List<Sumggle> list) {

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(150);
        animator.setAddDuration(150);
        animator.setChangeDuration(150);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        final SumggleAdapter adapter = new SumggleAdapter(list, context);
        recyclerView.setAdapter(adapter);
        /*
        * 添加新分区
        * */
        if(sum!=null && str.equals("create")) {
            adapter.add(sum);
            recyclerView.scrollToPosition(0);
        }


        /*
        * 更新分区名
        * */



        /*
        * 点击分区
        * */



        /*
        * 删除分区
        * */

    }


}
