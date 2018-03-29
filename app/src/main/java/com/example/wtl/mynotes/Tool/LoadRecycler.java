package com.example.wtl.mynotes.Tool;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wtl.mynotes.Activity.MainActivity;
import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.R;

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
        animation = AnimationUtils.loadAnimation(context, R.anim.change_list_anim_in);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        final NotesAdapter adapter = new NotesAdapter(list,context);
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
            public void OnItemClick() {

            }
        });
    }
    /*
    * 加载card布局
    * */
    public static void cardlist(RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        animation = AnimationUtils.loadAnimation(context,R.anim.change_list_anim_in);
        GridLayoutManager manager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(manager);
        Notes2Adapter adapter = new Notes2Adapter(list,context);
        recyclerView.setAdapter(adapter);
        recyclerView.startAnimation(animation);
    }

}
