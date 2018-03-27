package com.example.wtl.mynotes.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.wtl.mynotes.Adapter.SumggleAdapter;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Create_Dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SmuggleActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_sumggle;
    private ImageView sumggle_back;
    private RecyclerView smuggle_list;
    private List<Sumggle> sumggleList = new ArrayList<>();

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
        addAllSumggle();
    }

    private void Montior() {
        add_sumggle = (FloatingActionButton) findViewById(R.id.add_sumggle);
        smuggle_list = (RecyclerView) findViewById(R.id.smuggle_list);
        sumggle_back = (ImageView) findViewById(R.id.sumggle_back);

        add_sumggle.setOnClickListener(this);
        sumggle_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_sumggle:
                createDialog = new Create_Dialog(this);
                createDialog.setCanceledOnTouchOutside(false);
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
                        if(Create_Dialog.flag == 0) {
                            createDialog.dismiss();
                            addSumggle();
                        }
                    }
                });
                break;
            case R.id.sumggle_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
        }
    }

    //全局加载自定义文件夹
    private void addAllSumggle() {
        Cursor cursor = writebase.query(NotesDB.NOTECLIP_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
            do{
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                Sumggle sumggle = new Sumggle(memo,"0");
                sumggleList.add(sumggle);
            }while (cursor.moveToPrevious());
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        smuggle_list.setLayoutManager(manager);
        SumggleAdapter adapter = new SumggleAdapter(sumggleList,this);
        smuggle_list.setAdapter(adapter);
    }

    //添加自定义文件夹
    private void addSumggle() {
        Cursor cursor = writebase.query(NotesDB.NOTECLIP_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                Sumggle sumggle = new Sumggle(memo,"0");
                sumggleList.add(sumggle);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        smuggle_list.setLayoutManager(manager);
        SumggleAdapter adapter = new SumggleAdapter(sumggleList,this);
        smuggle_list.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SmuggleActivity.this.finish();
        overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
        return super.onKeyDown(keyCode, event);
    }
}
