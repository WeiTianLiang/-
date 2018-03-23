package com.example.wtl.mynotes.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        readDbase();
    }

    private void Montior() {
        add_my_notes = (FloatingActionButton) findViewById(R.id.add_my_notes);

        add_my_notes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_my_notes:
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
        }
    }
    /*
    * 数据库读值
    * */
    private void readDbase() {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
        if(cursor.moveToLast()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToPrevious());
            notes_list = (RecyclerView) findViewById(R.id.notes_list);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            notes_list.setLayoutManager(manager);
            NotesAdapter adapter = new NotesAdapter(notesList,this);
            adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int postion) {
                    Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                }
            });
            adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
                @Override
                public void OnItemLongClick(View view, int poetion) {

                }
            });
            notes_list.setAdapter(adapter);
        }
    }
}
