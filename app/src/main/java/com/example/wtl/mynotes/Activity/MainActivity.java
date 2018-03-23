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
                finish();
                break;
        }
    }

    private void intentnotes() {
        Notes notes = getIntent().getParcelableExtra("flag");
        if(notes != null) {
            notesList.add(notes);
            notes_list = (RecyclerView) findViewById(R.id.notes_list);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            notes_list.setLayoutManager(manager);
            NotesAdapter adapter = new NotesAdapter(notesList,this);
            notes_list.setAdapter(adapter);
        }
    }
    /*
    * 数据库读值
    * */
    private void readDbase() {
        Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                Notes notes = new Notes(content,time);
                notesList.add(notes);
            } while (cursor.moveToNext());
            notes_list = (RecyclerView) findViewById(R.id.notes_list);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            notes_list.setLayoutManager(manager);
            NotesAdapter adapter = new NotesAdapter(notesList,this);
            notes_list.setAdapter(adapter);
        }
    }
}
