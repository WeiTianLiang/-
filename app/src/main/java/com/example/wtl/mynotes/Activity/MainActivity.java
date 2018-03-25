package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_my_notes;

    private RecyclerView notes_list;
    private List<Notes> notesList = new ArrayList<>();

    private NotesDB notesDB;
    private SQLiteDatabase readbase;

    private ImageView change_list;//布局图片
    private Animation change_img;

    private Animation change_list_in;

    private SharedPreferences preferences;//判断程序是否第一次启动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * 沉浸式状态栏
        * */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //让标题栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //让标题栏处于亮色模式
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        preferences = getSharedPreferences("first_act",0);
        Boolean user_first = preferences.getBoolean("FIRST",true);
        if(user_first){//第一次
            preferences.edit().putBoolean("FIRST", false).commit();
            readDbase(0);
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
                change_list.setImageResource(R.mipmap.cardview);
            } else {
                change_list.setImageResource(R.mipmap.listview);
            }
            readDbase(x);
        }
    }

    private void Montior() {
        add_my_notes = (FloatingActionButton) findViewById(R.id.add_my_notes);
        change_list = (ImageView) findViewById(R.id.change_list);

        add_my_notes.setOnClickListener(this);
        change_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_my_notes:
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                finish();
                break;
            case R.id.change_list:
                ContentValues cv = new ContentValues();
                change_img = AnimationUtils.loadAnimation(this,R.anim.change_list_image_anim);
                if(change_list.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    change_list.setImageResource(R.mipmap.cardview);
                    change_list.startAnimation(change_img);
                    cardlist();
                    cv.put(NotesDB.FORMAT,1);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                } else {
                    change_list.setImageResource(R.mipmap.listview);
                    change_list.startAnimation(change_img);
                    loadlist();
                    cv.put(NotesDB.FORMAT,0);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                }
                break;
        }
    }
    /*
    * 数据库读值
    * */
    private void readDbase(int x) {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
        }
        if(x == 0) loadlist();
        else cardlist();
    }
    /*
    * 加载list布局
    * */
    private void loadlist() {
        change_list_in = AnimationUtils.loadAnimation(this,R.anim.change_list_anim_in);
        notes_list = (RecyclerView) findViewById(R.id.notes_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        notes_list.setLayoutManager(manager);
        NotesAdapter adapter = new NotesAdapter(notesList,this);
        notes_list.setAdapter(adapter);
        notes_list.startAnimation(change_list_in);
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int postion) {
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                finish();
            }
        });
    }
    /*
    * 加载card布局
    * */
    private void cardlist() {
        change_list_in = AnimationUtils.loadAnimation(this,R.anim.change_list_anim_in);
        notes_list = (RecyclerView) findViewById(R.id.notes_list);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        notes_list.setLayoutManager(manager);
        Notes2Adapter adapter = new Notes2Adapter(notesList,this);
        notes_list.setAdapter(adapter);
        notes_list.startAnimation(change_list_in);
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int postion) {
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                finish();
            }
        });
    }
}
