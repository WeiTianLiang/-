package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.LoadRecycler;
import com.example.wtl.mynotes.Tool.RandomColor;
import com.example.wtl.mynotes.Tool.ReadCuesor;
import com.example.wtl.mynotes.Tool.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class CstomSumActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView custom_recycler;
    private Animation animation;
    private Animation change_img;
    private Animation delete_down;
    private List<Notes> notesList = new ArrayList<>();
    private ImageView custom_list;
    private ImageView custom_back;
    private TextView custom_name;
    private TextView custom_num;
    private LinearLayout handle2;

    private SharedPreferences preferences;

    private FloatingActionButton add_custom;
    private LinearLayout custom_delet;

    private String str;
    private String str1;

    private IntentFilter intentFilter;
    private int color;

    private NotesDB notesDB;
    private SQLiteDatabase database;

    private String judgeSize;
    private String name;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cstom_sum);
        Montior();
        getIntentTransion();
        setbackgroundcolor();
        StatusBarUtils.setWindowStatusBarColor(this,color);
    }

    private void Montior() {
        custom_recycler = (RecyclerView) findViewById(R.id.custom_recycler);
        custom_list = (ImageView) findViewById(R.id.custom_list);
        custom_back = (ImageView) findViewById(R.id.custom_back);
        custom_name = (TextView) findViewById(R.id.custom_name);
        custom_num = (TextView) findViewById(R.id.custom_num);
        add_custom = (FloatingActionButton) findViewById(R.id.add_custom);
        custom_delet = (LinearLayout) findViewById(R.id.custom_delet);
        handle2 = (LinearLayout) findViewById(R.id.handle2);

        custom_list.setOnClickListener(this);
        custom_back.setOnClickListener(this);
        add_custom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_custom:
                Intent intent = new Intent(CstomSumActivity.this,EditNoteActivity.class);
                intent.putExtra("State","add");
                intent.putExtra("back","1");//记录返回的界面，0代表MainActivity，1代表HandleActivity
                intent.putExtra("color","white");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.custom_back:
                finish();
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                break;
            case R.id.custom_list:

                break;
        }
    }

    /*
    * 加载当前背景色
    * 如果第一次进入则随机产生一个
    * 如果不是则加载之前的背景色
    * */
    private void setbackgroundcolor() {
        notesDB = new NotesDB(this);
        database = notesDB.getWritableDatabase();
        /*
        * 统计表thecolor的数量
        * */
        String sql1 = "Select count(*) from thecolor";
        SQLiteStatement statement = database.compileStatement(sql1);
        /*
        * 转化为long
        * */
        long count = statement.simpleQueryForLong();
        /*
        * 判断：如果不是第一次进入则读取之前的背景
        * 否则随机产生一个
        * */
        if(count == Integer.parseInt(judgeSize)) {
            String sql = "select * from thecolor where memo='" + name + "'";
            Cursor cursor = database.rawQuery(sql,null);
            if(cursor.moveToFirst()) {
                color = cursor.getInt(cursor.getColumnIndex("colorstyle"));
            }
        } else {
            color = RandomColor.RandomColor();
            ContentValues cv = new ContentValues();
            cv.put(NotesDB.COLOR_STYLE,color);
            cv.put(NotesDB.COLOR_MEMO,name);
            database.insert(NotesDB.COLOR_NAME,null,cv);
        }
        /*
        * 设置背景色
        * */
        handle2.setBackground(getResources().getDrawable(color));
        add_custom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(color)));
    }

    /*
    * 获取intent传来的值，并加载到界面上
    * */
    private void getIntentTransion(){
        name = getIntent().getStringExtra("cstomName");
        num = getIntent().getStringExtra("cstomNum");
        judgeSize = getIntent().getStringExtra("cstomSize");
        custom_name.setText(name);
        custom_num.setText(num);
    }
}
