package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView edit_back;//返回
    private TextView edit_time;//时间
    private TextView edit_over;//完成
    private EditText edit_content;//内容
    private ImageView editcolor;//颜色
    private ImageView editbold;//加粗
    private ImageView editoblique;//斜体
    private ImageView editcenter;//居中
    private ImageView editdelet;//删除
    private ImageView editpicture;//相册

    private NotesDB notesDB;//初始化数据库
    private SQLiteDatabase writebase;//写数据库

    private Animation animation_show;//淡出动画
    private Animation animation_hide;//淡入动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Montior();
        animation_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edit_show);
        animation_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edit_hide);
        //EditText动态监听
        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edit_content.getText().toString().equals("")) {
                    edit_over.setVisibility(View.VISIBLE);
                    edit_over.setAnimation(animation_show);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edit_content.getText().toString().equals("")) {
                    edit_over.setVisibility(View.GONE);
                    edit_over.setAnimation(animation_hide);
                }
            }
        });
        edit_time.setText(getTime());
        notesDB = new NotesDB(this);
        writebase = notesDB.getWritableDatabase();
    }

    private void Montior() {
        edit_back = (ImageView) findViewById(R.id.edit_back);
        edit_time = (TextView) findViewById(R.id.edit_time);
        edit_over = (TextView) findViewById(R.id.edit_over);
        edit_content = (EditText) findViewById(R.id.edit_content);
        editcolor = (ImageView) findViewById(R.id.editcolor);
        editbold = (ImageView) findViewById(R.id.editbold);
        editoblique = (ImageView) findViewById(R.id.editoblique);
        editcenter = (ImageView) findViewById(R.id.editcenter);
        editdelet = (ImageView) findViewById(R.id.editdelet);
        editpicture = (ImageView) findViewById(R.id.editpicture);

        edit_back.setOnClickListener(this);
        edit_over.setOnClickListener(this);
        edit_content.setOnClickListener(this);
        editcolor.setOnClickListener(this);
        editbold.setOnClickListener(this);
        editoblique.setOnClickListener(this);
        editcenter.setOnClickListener(this);
        editdelet.setOnClickListener(this);
        editpicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(EditNoteActivity.this,MainActivity.class);
        switch (view.getId()) {
            case R.id.edit_back:
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);//设置activity的平移动画
                finish();
                break;
            case R.id.edit_over:
                if (!edit_content.getText().toString().equals("")) {
                    ContentValues cv = new ContentValues();
                    cv.put(NotesDB.CONTENT, edit_content.getText().toString());
                    cv.put(NotesDB.TIME, getTime());
                    writebase.insert(NotesDB.TABLE_NAME, null, cv);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                    finish();
                } else {
                    finish();
                    overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                    finish();
                }
                break;
        }
    }
    /*
    * 获取当前时间
    * */
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date();
        String time = format.format(date);
        return time;
    }
    /*
    * 监听返回键
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(EditNoteActivity.this,MainActivity.class);
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(intent);
            overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
            finish();
        }
        return false;
    }
}
