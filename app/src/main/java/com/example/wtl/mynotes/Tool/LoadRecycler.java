package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
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
        final List<Integer> stringList = new ArrayList<>();
        animation = AnimationUtils.loadAnimation(context, R.anim.change_list_anim_in);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        final NotesAdapter adapter = new NotesAdapter(list, context);
        recyclerView.setAdapter(adapter);
        recyclerView.startAnimation(animation);
        adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        });
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int x, boolean adro) {
                if(adro) {
                    stringList.add(x);
                }
                if(!adro){
                    for(int i = 0 ; i < stringList.size() ; i++) {
                        if(stringList.get(i) == x) {
                            stringList.remove(i);
                        }
                    }
                }
                Collections.sort(stringList);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0 ; i < stringList.size() ; i++) {
                    if(i == 0) adapter.removeNotes(stringList.get(i));
                    else adapter.removeNotes(stringList.get(i)-1);

                }
                stringList.removeAll(stringList);
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
