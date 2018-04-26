package com.example.wtl.mynotes.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Create_Dialog;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.LoadRecycler;
import com.example.wtl.mynotes.Tool.LoadSmuggle;
import com.example.wtl.mynotes.Tool.ReadCuesor;
import com.example.wtl.mynotes.Tool.ReadSmuggle;

import java.util.ArrayList;
import java.util.List;

public class SmuggleActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_sumggle;
    private ImageView sumggle_back;
    private TextView sumggle_text_back;
    private RecyclerView smuggle_list;
    private List<Sumggle> sumggleList = new ArrayList<>();
    private ImageView set_up;
    /*
    * 删除的数量
    * */
    private TextView delete_number;

    private Create_Dialog createDialog;

    private LinearLayout sumggle_handle;//随手记
    private LinearLayout sumggle_delete;//垃圾

    private NotesDB notesDB;
    private SQLiteDatabase database;

    private IntentFilter intentFilter;
    private TextView handle_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smuggle);
        Montior();
        HideScreenTop.HideScreenTop(getWindow());
        ReadSmuggle.ReadSmuggle(this,sumggleList,smuggle_list);
        deleteNum();
        intentFilter = new IntentFilter("com.example.wtl.mynotes.action");
        registerReceiver(broadcastReceiver,intentFilter);
        handleNum();
    }

    private void Montior() {
        add_sumggle = (FloatingActionButton) findViewById(R.id.add_sumggle);
        smuggle_list = (RecyclerView) findViewById(R.id.smuggle_list);
        sumggle_back = (ImageView) findViewById(R.id.sumggle_back);
        sumggle_text_back = (TextView) findViewById(R.id.sumggle_text_back);
        sumggle_handle = (LinearLayout) findViewById(R.id.sumggle_handle);
        sumggle_delete = (LinearLayout) findViewById(R.id.sumggle_delete);
        set_up = (ImageView) findViewById(R.id.set_up);
        delete_number = (TextView) findViewById(R.id.delete_number);
        handle_number = (TextView) findViewById(R.id.handle_number);

        add_sumggle.setOnClickListener(this);
        sumggle_back.setOnClickListener(this);
        sumggle_handle.setOnClickListener(this);
        sumggle_text_back.setOnClickListener(this);
        sumggle_delete.setOnClickListener(this);
        set_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent1;
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
                    public void ontureClick(String s) {
                        if(Create_Dialog.flag == 0) {
                            createDialog.dismiss();
                            Sumggle sumggle = new Sumggle(s,"0");
                            LoadSmuggle.loadSmuggle("create",sumggle,smuggle_list,SmuggleActivity.this,sumggleList);
                        }
                    }
                });
                break;
            case R.id.sumggle_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.sumggle_text_back:
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
        return super.onKeyDown(keyCode, event);
    }

    /*
    * 删除的数目
    * */
    private void deleteNum() {
        notesDB = new NotesDB(this);
        database = notesDB.getWritableDatabase();
        String sql = "Select count(*) from deleted";
        SQLiteStatement statement = database.compileStatement(sql);
        String count = statement.simpleQueryForString();
        delete_number.setText(count);
    }

    /*
    * 展示的数目
    * */
    private void handleNum() {
        notesDB = new NotesDB(this);
        database = notesDB.getWritableDatabase();
        String sql = "Select count(*) from notes";
        SQLiteStatement statement = database.compileStatement(sql);
        String count = statement.simpleQueryForString();
        handle_number.setText(count);
    }

    /*
    * 广播接收器
    * */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notesDB = new NotesDB(context);
            database = notesDB.getWritableDatabase();
            String sql = "Select count(*) from deleted";
            SQLiteStatement statement = database.compileStatement(sql);
            String count = statement.simpleQueryForString();
            delete_number.setText(count);
        }
    };
}
