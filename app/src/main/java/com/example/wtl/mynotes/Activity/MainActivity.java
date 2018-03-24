package com.example.wtl.mynotes.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

    private ImageView change_list;
    private Animation change_img;

    private Animation change_list_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        Cursor cursor = readbase.query(NotesDB.FORMAT_NAME,null,null,null,null,null,null);
        int x = 0;
        if(cursor.moveToFirst()) {
            x = cursor.getInt(cursor.getColumnIndex("form"));
        }
        Log.d("asd", String.valueOf(x));
        readDbase(x);
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
                change_img = AnimationUtils.loadAnimation(this,R.anim.change_list_image_anim);
                if(change_list.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    change_list.setImageResource(R.mipmap.cardview);
                    change_list.startAnimation(change_img);
                    readDbase(1);
                } else {
                    change_list.setImageResource(R.mipmap.listview);
                    change_list.startAnimation(change_img);
                    readDbase(0);
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
