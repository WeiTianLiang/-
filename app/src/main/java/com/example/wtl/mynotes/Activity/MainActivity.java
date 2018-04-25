package com.example.wtl.mynotes.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Creat_Search_Dialog;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.IsFirstOpen;
import com.example.wtl.mynotes.Tool.LoadRecycler;
import com.example.wtl.mynotes.Tool.ReadCuesor;
import com.example.wtl.mynotes.Tool.TimerTaskSearch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton add_my_notes;
    private LinearLayout item_delet;
    private EditText search_all;

    private RecyclerView notes_list;
    private List<Notes> notesList = new ArrayList<>();

    private NotesDB notesDB;
    private SQLiteDatabase readbase;
    private ImageView library;

    private ImageView change_list;//布局图片
    private Animation change_img;

    private Animation change_list_in;
    private Animation delete_down;
    private Animation search_gone;
    private Animation search_visib;

    private SharedPreferences preferences;//判断程序是否第一次启动
    /*
    * 创建搜索弹窗
    * */
    private Creat_Search_Dialog search_dialog = null;

    private IntentFilter intentFilter;
    /*
    * 接收广播传来的数据
    * */
    private String str;
    private String str1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideScreenTop.HideScreenTop(getWindow());//隐藏状态栏
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        initAnimation();
        preferences = getSharedPreferences("first_act", 0);
        Boolean user_first = preferences.getBoolean("FIRST", true);
        IsFirstOpen.IsFirstOpen(0, add_my_notes, item_delet, change_list, preferences, user_first, this, readbase, notesList, notes_list, change_list_in);
        search_all.setInputType(InputType.TYPE_NULL);//让主页面的edittext无法弹出输入框
        search_all.setOnTouchListener(new View.OnTouchListener() {
            int flag = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag++;
                if (flag == 2) {
                    flag = 0;
                    TimerTaskSearch.TimerTaskSearch(search_all,search_gone);//延迟搜索框消失
                    search_dialog = new Creat_Search_Dialog(MainActivity.this, search_all);
                    search_dialog.show();
                    Window window = search_dialog.getWindow();
                    window.setGravity(Gravity.TOP);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                            | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    search_dialog.setBackClickListener(new Creat_Search_Dialog.OnBackClickListener() {
                        @Override
                        public void onbackclick() {
                            search_dialog.dismiss();
                            search_all.setVisibility(View.VISIBLE);
                            search_all.setAnimation(search_visib);
                        }
                    });
                }
                return true;
            }
        });
        intentFilter = new IntentFilter("com.example.wtl.mynotes.action");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    private void Montior() {
        add_my_notes = (FloatingActionButton) findViewById(R.id.add_my_notes);
        change_list = (ImageView) findViewById(R.id.change_list);
        library = (ImageView) findViewById(R.id.library);
        notes_list = (RecyclerView) findViewById(R.id.notes_list);
        item_delet = (LinearLayout) findViewById(R.id.item_delet);
        search_all = (EditText) findViewById(R.id.search_all);

        add_my_notes.setOnClickListener(this);
        change_list.setOnClickListener(this);
        library.setOnClickListener(this);
        search_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_my_notes:
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("State", "add");
                intent.putExtra("back", "0");
                intent.putExtra("color", "white");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;
            case R.id.change_list:
                ContentValues cv = new ContentValues();
                if (change_list.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    change_list.setImageResource(R.mipmap.cardview);
                    change_list.startAnimation(change_img);
                    LoadRecycler.cardlist(null,null,null,ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, this, notesList);
                    cv.put(NotesDB.FORMAT, 1);
                    readbase.insert(NotesDB.FORMAT_NAME, null, cv);
                } else {
                    change_list.setImageResource(R.mipmap.listview);
                    change_list.startAnimation(change_img);
                    LoadRecycler.loadlist(null,null,null,ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, this, notesList);
                    cv.put(NotesDB.FORMAT, 0);
                    readbase.insert(NotesDB.FORMAT_NAME, null, cv);
                }
                if (item_delet.getVisibility() == View.VISIBLE) {
                    add_my_notes.setVisibility(View.VISIBLE);
                    item_delet.setVisibility(View.GONE);
                    item_delet.startAnimation(delete_down);
                }
                break;
            case R.id.library:
                Intent intent1 = new Intent(MainActivity.this, SmuggleActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                break;
        }
    }

    //初始化动画
    private void initAnimation() {
        delete_down = AnimationUtils.loadAnimation(this, R.anim.delete_down);
        change_img = AnimationUtils.loadAnimation(this, R.anim.change_list_image_anim);
        search_gone = AnimationUtils.loadAnimation(this, R.anim.search_gone);
        search_visib = AnimationUtils.loadAnimation(this, R.anim.search_visib);
    }

    /*
    * 广播接收器
    * */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            str = intent.getExtras().getString("createNew");
            str1 = intent.getExtras().getString("recoy");
            /*
            * 添加后更新recyclerview数据
            * */
            if(str!=null&&str.equals("create")) {
                Notes notes = intent.getParcelableExtra("notes");
                if(change_list.getDrawable().getCurrent().getConstantState().
                        equals(MainActivity.this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    LoadRecycler.loadlist(null,"create",notes, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, notesList);
                } else {
                    LoadRecycler.cardlist(null,"create",notes, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, notesList);
                }
            }
            /*
            * 修改后更新recyclerview数据
            * */
            else if(str!=null&&str.equals("update")) {
                Notes notes = intent.getParcelableExtra("notes");
                String post = intent.getExtras().getString("point");
                if(change_list.getDrawable().getCurrent().getConstantState().
                        equals(MainActivity.this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    LoadRecycler.loadlist(post,"update",notes, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, notesList);
                } else {
                    LoadRecycler.cardlist(post,"update",notes, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, notesList);
                }
            }
            /*
            * 从数据库还原后更新recyclerview数据
            * */
            if(str1!=null&&str1.equals("yes")) {
                List<Notes> lists = new ArrayList<>();
                Cursor cursor = readbase.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);//查找数据到cursor对象
                List<String> color = new ArrayList<>();
                if(cursor.moveToLast()) {
                    do {
                        String content = cursor.getString(cursor.getColumnIndex("content"));
                        String time = cursor.getString(cursor.getColumnIndex("time"));
                        color.add(cursor.getString(cursor.getColumnIndex("color")));
                        Notes notes = new Notes(content,time);
                        lists.add(notes);
                    } while (cursor.moveToPrevious());
                }
                if(change_list.getDrawable().getCurrent().getConstantState().
                        equals(MainActivity.this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    LoadRecycler.loadlist(null,null,null, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, lists);
                } else {
                    LoadRecycler.cardlist(null,null,null, ReadCuesor.ReadColor(readbase), 0, add_my_notes, item_delet, notes_list, change_list_in, MainActivity.this, lists);
                }
            }
        }
    };

    /*
    * 销毁广播
    * */
    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
