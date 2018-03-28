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
import android.widget.Toast;

import com.example.wtl.mynotes.Adapter.Notes2Adapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.IsFirstOpen;
import com.example.wtl.mynotes.Tool.LoadRecycler;
import com.example.wtl.mynotes.Tool.ReadCuesor;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_my_notes;

    private RecyclerView notes_list;
    private List<Notes> notesList = new ArrayList<>();

    private NotesDB notesDB;
    private SQLiteDatabase readbase;
    private ImageView library;

    private ImageView change_list;//布局图片
    private Animation change_img;

    private Animation change_list_in;

    private SharedPreferences preferences;//判断程序是否第一次启动

    private List<Boolean> booleanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideScreenTop.HideScreenTop(getWindow());//隐藏状态栏
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        preferences = getSharedPreferences("first_act",0);
        Boolean user_first = preferences.getBoolean("FIRST",true);
        IsFirstOpen.IsFirstOpen(change_list,preferences,user_first,this,readbase,notesList,notes_list,change_list_in);
    }

    private void Montior() {
        add_my_notes = (FloatingActionButton) findViewById(R.id.add_my_notes);
        change_list = (ImageView) findViewById(R.id.change_list);
        library = (ImageView) findViewById(R.id.library);
        notes_list = (RecyclerView) findViewById(R.id.notes_list);

        add_my_notes.setOnClickListener(this);
        change_list.setOnClickListener(this);
        library.setOnClickListener(this);
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
                    LoadRecycler.cardlist(notes_list,change_list_in,this,notesList);
                    cv.put(NotesDB.FORMAT,1);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                } else {
                    change_list.setImageResource(R.mipmap.listview);
                    change_list.startAnimation(change_img);
                    LoadRecycler.loadlist(notes_list,change_list_in,this,notesList);
                    cv.put(NotesDB.FORMAT,0);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                }
                break;
            case R.id.library:
                Intent intent1 = new Intent(MainActivity.this,SmuggleActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                break;
        }
    }

}
