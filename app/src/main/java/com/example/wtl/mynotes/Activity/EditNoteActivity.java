package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Change_Colors;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.JudgeWordSize;
import com.example.wtl.mynotes.Tool.StatusBarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TAKE_PHONE = 1;

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

    private SQLiteDatabase writebase;//写数据库

    private Animation animation_show;//淡出动画
    private Animation animation_hide;//淡入动画
    private Animation animation_floar;//上浮动画

    /*
    * 四种字体是否选中的判断
    * */
    private boolean isBold = false;
    private boolean isLean = false;
    private boolean isBig = false;
    private boolean isPoint = false;
    /*
    * 输入的起始位置
    * */
    private int start;
    /*
    * 输入的数量
    * */
    private int count;
    /*
    * 记录删除数量
    * */
    private int delete;

    private boolean change_ov = true;//判断是修改还是新建,true为插入,false为修改
    private String change_time;//修改的时间
    private String sta = null;

    private LinearLayout change_bottom;
    private LinearLayout change_colors;
    private LinearLayout alledit;

    /*
    * 背景色
    * */
    private ImageView red;
    private ImageView blue;
    private ImageView green;
    private ImageView violet;
    private ImageView black;
    private ImageView white;
    private ImageView picture;
    private LinearLayout edit1;
    private LinearLayout edit3;
    private ImageView picture_show;

    private String color = "white";//初始背景颜色为白色
    private Uri uri;

    /*
    * 记录当前text状态的list
    * */
    private List<String> textstatelist = new ArrayList<>();
    /*
    * 记录所有text的状态
    * */
    private List<String> textState = new ArrayList<>();
    /*
    * 记录跳转之前的位置
    * */
    private String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        HideScreenTop.HideScreenTop(getWindow());
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);
        Montior();
        animation_show = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edit_show);
        animation_hide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edit_hide);
        /*
        * 初始化数据库
        * */
        NotesDB notesDB = new NotesDB(this);
        /*
        * 初始化数据库工具
        * */
        writebase = notesDB.getWritableDatabase();
        Intent intent = getIntent();
        /*
        * 判断当前是修改状态还是创建状态
        * 必须在edit_content.addTextChangedListener之前，要先判断修改再进行增加
        * 因为都要调用状态修饰的editchange，如果新建在前则会二次调用
        * */
        String state = intent.getStringExtra("State");
        if (state.equals("change")) {
            showcontent();
        }
        /*
        * 取消背景栏
        * */
        edit_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_bottom.setVisibility(View.VISIBLE);
                change_colors.setVisibility(View.GONE);
                editcolor.setImageResource(R.mipmap.editcolor);
            }
        });
        /*
        * EditText动态监听
        * */
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
                delete = i1;
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
        /*
        * 显示时间
        * */
        edit_time.setText(getTime());
        /*
        * 初始化数据库
        * */
        notesDB = new NotesDB(this);
        /*
        * 初始化数据库工具
        * */
        writebase = notesDB.getWritableDatabase();
        /*
        * 获取改activity是从哪个activity跳转而来的
        * */
        sta = intent.getStringExtra("back");
        /*
        * 获取当前背景色
        * */
        String back_color = intent.getStringExtra("color");
        Change_Colors.Change_Colors(this, back_color, alledit, edit_content, edit1, edit_time, edit3);//改变背景色
        /*
        * 加载设置的字体大小
        * */
        WordSize();
        /*
        * 添加一个基础normal状态
        * */
        textstatelist.add("normal");
        /*
        * 让光标移到最后
        * */
        edit_content.setSelection(edit_content.getText().toString().length());
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
        picture = (ImageView) findViewById(R.id.editpicture);
        picture_show = (ImageView) findViewById(R.id.picture_show);

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
        picture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        animation_floar = AnimationUtils.loadAnimation(this, R.anim.delete_floar);
        switch (view.getId()) {
            case R.id.edit_back:
                finish();
                overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);//设置activity的平移动画
                break;
            case R.id.edit_over:
                addCritical();
                if (change_ov) {
                    /*
                    * 写数据库
                    * */
                    ContentValues cv = new ContentValues();
                    /*
                    * 写入文本内容
                    * */
                    cv.put(NotesDB.CONTENT, edit_content.getText().toString());
                    /*
                    * 写入当前时间
                    * */
                    cv.put(NotesDB.TIME, getTime());
                    /*
                    * 写入背景色
                    * */
                    cv.put(NotesDB.COLOR, color);
                    /*
                    * 写入文本的各个分类情况
                    * */
                    cv.put(NotesDB.STATETEXT, listToString(textState));
                    writebase.insert(NotesDB.TABLE_NAME, null, cv);
                    Intent intent = new Intent("com.example.wtl.mynotes.action");
                    intent.putExtra("createNew", "create");
                    Notes notes = new Notes(edit_content.getText().toString(), getTime());
                    intent.putExtra("notes", notes);
                    sendBroadcast(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                } else {
                    //升级数据库
                    ContentValues cv = new ContentValues();
                    cv.put(NotesDB.TIME, getTime());
                    cv.put(NotesDB.CONTENT, edit_content.getText().toString());
                    cv.put(NotesDB.COLOR, color);
                    cv.put(NotesDB.STATETEXT, listToString(textState));
                    writebase.update(NotesDB.TABLE_NAME, cv, NotesDB.TIME + "=?", new String[]{change_time});
                    Intent intent = new Intent("com.example.wtl.mynotes.action");
                    intent.putExtra("createNew", "update");
                    intent.putExtra("point", post);
                    Notes notes = new Notes(edit_content.getText().toString(), getTime());
                    intent.putExtra("notes", notes);
                    sendBroadcast(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                }
                break;
            case R.id.editbold:
                if (editbold.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editbold).getConstantState())) {
                    editbold.setImageResource(R.mipmap.touchblod);
                    isBold = true;
                    textstatelist.add("blod");
                } else {
                    editbold.setImageResource(R.mipmap.editbold);
                    isBold = false;
                    textStateDelete(textstatelist, "blod");
                }
                break;
            case R.id.editoblique:
                if (editoblique.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editoblique).getConstantState())) {
                    editoblique.setImageResource(R.mipmap.touchoblique);
                    isLean = true;
                    textstatelist.add("lean");
                } else {
                    editoblique.setImageResource(R.mipmap.editoblique);
                    isLean = false;
                    textStateDelete(textstatelist, "lean");
                }
                break;
            case R.id.editcenter:
                if (editcenter.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editbig).getConstantState())) {
                    editcenter.setImageResource(R.mipmap.toucheditbig);
                    isBig = true;
                    textstatelist.add("center");
                } else {
                    editcenter.setImageResource(R.mipmap.editbig);
                    isBig = false;
                    textStateDelete(textstatelist, "center");
                }
                break;
            case R.id.editpoint:
                if (editpoint.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.editpoint).getConstantState())) {
                    editpoint.setImageResource(R.mipmap.toucheditpoint);
                    isPoint = true;
                    textstatelist.add("point");
                } else {
                    editpoint.setImageResource(R.mipmap.editpoint);
                    isPoint = false;
                    textStateDelete(textstatelist, "point");
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
                Change_Colors.Change_Colors(this, "white", alledit, edit_content, edit1, edit_time, edit3);
                color = "white";
                break;
            case R.id.red:
                Change_Colors.Change_Colors(this, "red", alledit, edit_content, edit1, edit_time, edit3);
                color = "red";
                break;
            case R.id.green:
                Change_Colors.Change_Colors(this, "green", alledit, edit_content, edit1, edit_time, edit3);
                color = "green";
                break;
            case R.id.blue:
                Change_Colors.Change_Colors(this, "blue", alledit, edit_content, edit1, edit_time, edit3);
                color = "blue";
                break;
            case R.id.violet:
                Change_Colors.Change_Colors(this, "violet", alledit, edit_content, edit1, edit_time, edit3);
                color = "violet";
                break;
            case R.id.black:
                Change_Colors.Change_Colors(this, "black", alledit, edit_content, edit1, edit_time, edit3);
                color = "black";
                break;
            case R.id.editpicture:
                File outputimage = new File(getExternalCacheDir(), "out_image.jpg");
                try {
                    if (outputimage.exists()) {
                        outputimage.delete();
                    }
                    outputimage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(EditNoteActivity.this, "com.example.cameraalbumtest.fileprovider", outputimage);
                } else {
                    uri = Uri.fromFile(outputimage);
                }
                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent2, TAKE_PHONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHONE:
                if (requestCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        picture_show.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
        }
        return false;
    }

    /*
    * 实现文本的即使加粗斜体。。。
    * */
    private void editchange(Editable s) {
        if (count != 0) {
            for (int i = start; i < start + count; i++) {
                String Tstate = "";
                if (isBold) {
                    s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Tstate += "blod|";
                }
                if (isLean) {
                    s.setSpan(new StyleSpan(Typeface.ITALIC), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Tstate += "lean|";
                }
                if (isBig) {
                    s.setSpan(new RelativeSizeSpan(2.0f), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Tstate += "big|";
                }
                if (isPoint) {
                    s.setSpan(new ForegroundColorSpan(Color.BLUE), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Tstate += "point|";
                }
                if (!isBold && !isPoint && !isBig && !isLean) {
                    Tstate += "normal|";
                }
                textState.add(i, Tstate);
            }
        } else {
            /*
            * 根据删除的个数删除相应数量的状态
            * */
            for (int i = 0 ; i < delete ;i++) {
                /*
                * 删除时list总数量会减少，所以恒删除一个位置就是挨个删除
                * */
                textState.remove(start);
            }
        }
    }

    /*
    * 将list转化为string，因为数据库只能存储扁平化的数据，所以需要将list转化为string
    * */
    private String listToString(List<String> list) {
        String str = "";
        for (String s : list) {
            str += s;
        }
        return str;
    }

    /*
    * 进入该活动应显示的值并修改
    * */
    private void showcontent() {
        /*
        * 观察模式，edittext不会自动获取焦点
        * */
        alledit.setFocusable(true);
        alledit.setFocusableInTouchMode(true);
        /*
        * 将状态改为修改状态
        * */
        change_ov = false;
        /*
        * 接受从跳转方传来的信息
        * */
        Intent intent = getIntent();
        String postion = intent.getStringExtra("Postion");
        post = intent.getStringExtra("post");
        String sql = "select*from notes where time= '" + postion + "'";
        Cursor cursor = writebase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            /*
            * 从数据库中读取当前应显示的值
            * */
            String content = cursor.getString(cursor.getColumnIndex("content"));
            /*
            * 从数据库中读取当前应显示值的所有状态
            * contentStateNum:数量
            * contentStateText:对应的状态
            * */
            String contentStateText = cursor.getString(cursor.getColumnIndex("statetext"));
            AnalysisStringShow(contentStateText, content);
        }
        change_time = postion;
    }

    /*
    * 解析从数据库中传来的当前便签的文字对应的状态数量及状态种类
    * */
    private void AnalysisStringShow(String contentStateText, final String content) {
        /*
        * 将string类型转为edittext
        * */
        Editable editable = new SpannableStringBuilder(content);
        /*
        * 状态种类第一层解析
        * */
        String[] text = contentStateText.split("\\*");
        /*
        * 状态种类第二层解析
        * */
        String[][] textsplit = new String[text.length][];
        /*
        * 适配文字状态并显示
        * */
        for (int i = 0; i < text.length; i++) {
            textsplit[i] = text[i].split("\\|");
            for (String s : textsplit[i]) {
                /*
                * 根据读取到的状态信息改变状态
                * */
                if (s.equals("blod")) {
                    isBold = true;
                }
                if (s.equals("lean")) {
                    isLean = true;
                }
                if (s.equals("big")) {
                    isBig = true;
                }
                if (s.equals("point")) {
                    isPoint = true;
                }
            }
            /*
            * 给初始start和数量count赋值
            * */
            start = i;
            count = 1;
            /*
            * 重载editchange
            * */
            editchange(editable);
            /*
            * 取消四种状态避免叠加
            * */
            isBold = false;
            isLean = false;
            isBig = false;
            isPoint = false;
        }
        edit_content.setText(editable);
    }

    //修改显示便签时的字体
    private void WordSize() {
        /*
        * 从数据库获取当前字体大小
        * */
        String state = JudgeWordSize.JudgeWordSize(writebase);
        /*
        * 10号小字
        * */
        if (state.equals("litter")) {
            edit_content.setTextSize(10);
        }
        /*
        * 16号中字
        * */
        if (state.equals("ordinary")) {
            edit_content.setTextSize(16);
        }
        /*
        * 22号大字
        * */
        if (state.equals("large")) {
            edit_content.setTextSize(22);
        }
    }

    //将list中的值删除
    private void textStateDelete(List<String> list, String sta) {
        /*
        * 不能直接对list进行修改，list删除一个值只能对list对象的ModCount进行修改，
        * 无法对其迭代器Iterator的expectedModCount的值进行修改
        * 而对list的使用必须经过Iterator，所以会出现java.util.ConcurrentModificationException异常
        * CopyOnWriteArrayList是线程安全list
        * */
        final CopyOnWriteArrayList<String> copyOnWrite = new CopyOnWriteArrayList<>(list);
        for (String s : list) {
            if (s.equals(sta)) {
                copyOnWrite.remove(sta);
            }
        }
        /*
        * 删除该表中所有的数据
        * */
        list.removeAll(list);
        /*
        * 添加修改后的
        * */
        list.addAll(copyOnWrite);
    }

    /*
    * 向状态list添加分界字符
    * */
    private void addCritical() {
        List<String> listcopy = new ArrayList<>();
        for (String s : textState) {
            listcopy.add(s);
        }
        textState.removeAll(textState);
        for (int i = 0; i < listcopy.size(); i++) {
            textState.add(listcopy.get(i));
            textState.add("*");
        }
    }

}
