package com.example.wtl.mynotes.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Adapter.SumggleAdapter;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Create_Dialog;
import com.example.wtl.mynotes.Tool.HideScreenTop;

import java.util.ArrayList;
import java.util.List;

public class SmuggleActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_sumggle;
    private ImageView sumggle_back;
    private TextView sumggle_text_back;
    private RecyclerView smuggle_list;
    private List<Sumggle> sumggleList = new ArrayList<>();
    private ImageView set_up;

    private NotesDB notesDB;
    private SQLiteDatabase writebase;

    private Create_Dialog createDialog;

    private LinearLayout sumggle_handle;//随手记
    private LinearLayout sumggle_delete;//垃圾

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smuggle);
        Montior();
        notesDB = new NotesDB(this);
        writebase = notesDB.getWritableDatabase();
        HideScreenTop.HideScreenTop(getWindow());
        addAllSumggle();
    }

    private void Montior() {
        add_sumggle = (FloatingActionButton) findViewById(R.id.add_sumggle);
        smuggle_list = (RecyclerView) findViewById(R.id.smuggle_list);
        sumggle_back = (ImageView) findViewById(R.id.sumggle_back);
        sumggle_text_back = (TextView) findViewById(R.id.sumggle_text_back);
        sumggle_handle = (LinearLayout) findViewById(R.id.sumggle_handle);
        sumggle_delete = (LinearLayout) findViewById(R.id.sumggle_delete);
        set_up = (ImageView) findViewById(R.id.set_up);

        add_sumggle.setOnClickListener(this);
        sumggle_back.setOnClickListener(this);
        sumggle_handle.setOnClickListener(this);
        sumggle_text_back.setOnClickListener(this);
        sumggle_delete.setOnClickListener(this);
        set_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent1 = new Intent(SmuggleActivity.this,MainActivity.class);
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
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.sumggle_text_back:
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.sumggle_handle:
                intent1 = new Intent(SmuggleActivity.this,HandleActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.sumggle_delete:
                intent1 = new Intent(SmuggleActivity.this, AbandonActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.set_up:
                Intent intent = new Intent(SmuggleActivity.this,SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
        }
    }

    //全局加载自定义文件夹
    private void addAllSumggle() {
        Cursor cursor = writebase.query(NotesDB.NOTECLIP_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
            do{
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                Sumggle sumggle = new Sumggle(memo,"1");
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
        Intent intent = new Intent(SmuggleActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
        return super.onKeyDown(keyCode, event);
    }
}
