package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Change_Colors;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.StatusBarUtils;

import java.util.Date;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView edit_back;//返回
    private TextView edit_time;//时间
    private TextView edit_over;//完成
    private EditText edit_content;//内容

    private ImageView editcolor;//背景颜色
    private ImageView editbold;//加粗
    private ImageView editoblique;//斜体
    private ImageView editcenter;//大字
    private ImageView editpoint;//重点
    private ImageView editpicture;//相册

    private NotesDB notesDB;//初始化数据库
    private SQLiteDatabase writebase;//写数据库

    private Animation animation_show;//淡出动画
    private Animation animation_hide;//淡入动画
    private Animation animation_floar;//上浮动画

    private boolean isBold = false;
    private boolean isLean = false;
    private boolean isBig = false;
    private boolean isPoint = false;
    private int start;
    private int count;

    private boolean change_ov = true;//判断是修改还是新建
    private String change_time;//修改的时间
    private String sta = null;

    private LinearLayout change_bottom;
    private LinearLayout change_colors;
    private LinearLayout alledit;

    //更换背景色
    private ImageView red;
    private ImageView blue;
    private ImageView green;
    private ImageView violet;
    private ImageView black;
    private ImageView white;
    private LinearLayout edit1;
    private LinearLayout edit3;

    private String color = "white";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        HideScreenTop.HideScreenTop(getWindow());
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        Montior();
        animation_show = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edit_show);
        animation_hide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edit_hide);
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
                start = i;
                count = i2;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edit_content.getText().toString().equals("")) {
                    edit_over.setVisibility(View.GONE);
                    edit_over.setAnimation(animation_hide);
                }
                editchange(editable);
            }
        });
        edit_time.setText(getTime());
        notesDB = new NotesDB(this);
        writebase = notesDB.getWritableDatabase();
        Intent intent = getIntent();
        String state = intent.getStringExtra("State");
        if (state.equals("change")) {
            showcontent();
        }
        sta = intent.getStringExtra("back");
        String back_color = intent.getStringExtra("color");
        Change_Colors.Change_Colors(this,back_color,alledit,edit_content,edit1,edit_time,edit3);
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
        editpicture = (ImageView) findViewById(R.id.editpicture);
        editpoint = (ImageView) findViewById(R.id.editpoint);
        change_bottom = (LinearLayout) findViewById(R.id.edit3);
        change_colors = (LinearLayout) findViewById(R.id.change_colors);
        alledit = (LinearLayout) findViewById(R.id.alledit);

        red = (ImageView) findViewById(R.id.red);
        green = (ImageView) findViewById(R.id.green);
        blue = (ImageView) findViewById(R.id.blue);
        violet = (ImageView) findViewById(R.id.violet);
        black = (ImageView) findViewById(R.id.black);
        white = (ImageView) findViewById(R.id.white);
        edit1 = (LinearLayout) findViewById(R.id.edit1);
        edit3 = (LinearLayout) findViewById(R.id.edit3);

        edit_back.setOnClickListener(this);
        edit_over.setOnClickListener(this);
        edit_content.setOnClickListener(this);

        editcolor.setOnClickListener(this);
        editbold.setOnClickListener(this);
        editoblique.setOnClickListener(this);
        editcenter.setOnClickListener(this);
        editpicture.setOnClickListener(this);
        editpoint.setOnClickListener(this);
        alledit.setOnClickListener(this);

        red.setOnClickListener(this);
        green.setOnClickListener(this);
        blue.setOnClickListener(this);
        violet.setOnClickListener(this);
        black.setOnClickListener(this);
        white.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
        Intent intent1 = new Intent(EditNoteActivity.this, HandleActivity.class);
        animation_floar = AnimationUtils.loadAnimation(this,R.anim.delete_floar);
        switch (view.getId()) {
            case R.id.edit_back:
                if(sta.equals("0")) {
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);//设置activity的平移动画
                } else if(sta.equals("1")) {
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                }
                break;
            case R.id.edit_over:
                if (change_ov) {
                    //写数据库
                    ContentValues cv = new ContentValues();
                    cv.put(NotesDB.CONTENT, edit_content.getText().toString());
                    cv.put(NotesDB.TIME, getTime());
                    cv.put(NotesDB.COLOR,color);
                    writebase.insert(NotesDB.TABLE_NAME, null, cv);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                    finish();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(NotesDB.TIME, getTime());
                    cv.put(NotesDB.CONTENT, edit_content.getText().toString());
                    cv.put(NotesDB.COLOR,color);
                    writebase.update(NotesDB.TABLE_NAME, cv, NotesDB.TIME + "=?", new String[]{change_time});
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                    finish();
                }
                break;
            case R.id.editbold:
                if (editbold.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editbold).getConstantState())) {
                    editbold.setImageResource(R.mipmap.touchblod);
                    isBold = true;
                } else {
                    editbold.setImageResource(R.mipmap.editbold);
                    isBold = false;
                }
                break;
            case R.id.editoblique:
                if (editoblique.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editoblique).getConstantState())) {
                    editoblique.setImageResource(R.mipmap.touchoblique);
                    isLean = true;
                } else {
                    editoblique.setImageResource(R.mipmap.editoblique);
                    isLean = false;
                }
                break;
            case R.id.editcenter:
                if (editcenter.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editbig).getConstantState())) {
                    editcenter.setImageResource(R.mipmap.toucheditbig);
                    isBig = true;
                } else {
                    editcenter.setImageResource(R.mipmap.editbig);
                    isBig = false;
                }
                break;
            case R.id.editpoint:
                if (editpoint.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editpoint).getConstantState())) {
                    editpoint.setImageResource(R.mipmap.toucheditpoint);
                    isPoint = true;
                } else {
                    editpoint.setImageResource(R.mipmap.editpoint);
                    isPoint = false;
                }
                break;
            case R.id.editcolor:
                if (editcolor.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editcolor).getConstantState())) {
                    editcolor.setImageResource(R.mipmap.toucheditcolor);
                    change_bottom.setVisibility(View.GONE);
                    change_colors.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.alledit:
                change_bottom.setVisibility(View.VISIBLE);
                change_colors.setVisibility(View.GONE);
                editcolor.setImageResource(R.mipmap.editcolor);
                break;
            case R.id.white:
                Change_Colors.Change_Colors(this,"white",alledit,edit_content,edit1,edit_time,edit3);
                color = "white";
                break;
            case R.id.red:
                Change_Colors.Change_Colors(this,"red",alledit,edit_content,edit1,edit_time,edit3);
                color = "red";
                break;
            case R.id.green:
                Change_Colors.Change_Colors(this,"green",alledit,edit_content,edit1,edit_time,edit3);
                color = "green";
                break;
            case R.id.blue:
                Change_Colors.Change_Colors(this,"blue",alledit,edit_content,edit1,edit_time,edit3);
                color = "blue";
                break;
            case R.id.violet:
                Change_Colors.Change_Colors(this,"violet",alledit,edit_content,edit1,edit_time,edit3);
                color = "violet";
                break;
            case R.id.black:
                Change_Colors.Change_Colors(this,"black",alledit,edit_content,edit1,edit_time,edit3);
                color = "black";
                break;

        }
    }

    /*
    * 获取当前时间
    * */
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        return time;
    }

    /*
    * 监听返回键
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
        Intent intent1 = new Intent(EditNoteActivity.this, HandleActivity.class);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(sta.equals("0")) {
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
            } else if(sta.equals("1")) {
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
            }
        }
        return false;
    }

    //实现即时文本字体改变
    private void editchange(Editable s) {
        if (isBold) {
            s.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isLean) {
            s.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isBig) {
            s.setSpan(new RelativeSizeSpan(2.0f), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isPoint) {
            s.setSpan(new ForegroundColorSpan(Color.BLUE), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    //进入该活动应显示的值并修改
    private void showcontent() {
        change_ov = false;
        Intent intent = getIntent();
        String postion = intent.getStringExtra("Postion");
        String sql = "select*from notes where time= '" + postion + "'";
        Cursor cursor = writebase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            edit_content.setText(content);
        }
        change_time = postion;
    }

}
