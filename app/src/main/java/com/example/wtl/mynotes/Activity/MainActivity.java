package com.example.wtl.mynotes.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.wtl.mynotes.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_my_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Montior();
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
                break;
        }
    }
}
