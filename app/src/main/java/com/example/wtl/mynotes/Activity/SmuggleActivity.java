package com.example.wtl.mynotes.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Create_Dialog;

public class SmuggleActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_sumggle;
    private RecyclerView smuggle_list;

    private NotesDB notesDB;
    private SQLiteDatabase writebase;

    private Create_Dialog createDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smuggle);
        Montior();
        notesDB = new NotesDB(this);
        writebase = notesDB.getWritableDatabase();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void Montior() {
        add_sumggle = (FloatingActionButton) findViewById(R.id.add_sumggle);

        add_sumggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_sumggle:
                createDialog = new Create_Dialog(this);
                createDialog.show();
                Window window = createDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                createDialog.setOnCancelClickListener(new Create_Dialog.OnCancelClickListener() {
                    @Override
                    public void oncancelClick() {
                        createDialog.dismiss();
                    }
                });
                createDialog.setOnTureClickListener(new Create_Dialog.OnTureClickListener() {
                    @Override
                    public void ontureClick() {
                        createDialog.dismiss();
                    }
                });
                break;
        }
    }
}
