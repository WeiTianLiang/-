package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.IsFirstOpen;
import com.example.wtl.mynotes.Tool.LoadRecycler;

import java.util.ArrayList;
import java.util.List;

public class AbandonActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView abandon_text_back;
    private RecyclerView abandon_recycler;
    private LinearLayout abandon_sum_delet;
    private Animation animation;
    private Animation change_img;
    private Animation delete_down;
    private ImageView abandon_recovey;
    private ImageView abandon_delete;
    private ImageView abandon_list;

    private NotesDB notesDB;
    private SQLiteDatabase readbase;

    private SharedPreferences preferences;
    private List<Notes> Lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abandon);
        HideScreenTop.HideScreenTop(getWindow());
        Montior();
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        preferences = getSharedPreferences("first_act",0);
        Boolean user_first = preferences.getBoolean("FIRST",true);
        delete_down = AnimationUtils.loadAnimation(this,R.anim.delete_down);
        IsFirstOpen.IsFirstOpen(abandon_sum_delet,abandon_delete,abandon_recovey,abandon_list,preferences,user_first,this,readbase,Lists,abandon_recycler,animation);
    }

    private void Montior() {
        abandon_text_back = (TextView) findViewById(R.id.abandon_text_back);
        abandon_recycler = (RecyclerView) findViewById(R.id.abandon_recycler);
        abandon_sum_delet = (LinearLayout) findViewById(R.id.abandon_sum_delet);
        abandon_recovey = (ImageView) findViewById(R.id.abandon_recovey);
        abandon_delete = (ImageView) findViewById(R.id.abandon_delete);
        abandon_list = (ImageView) findViewById(R.id.abandon_list);

        abandon_text_back.setOnClickListener(this);
        abandon_recycler.setOnClickListener(this);
        abandon_recovey.setOnClickListener(this);
        abandon_delete.setOnClickListener(this);
        abandon_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.abandon_text_back:
                finish();
                overridePendingTransition(R.anim.activity_right_in,R.anim.activity_right_out);
                break;
            case R.id.abandon_list:
                ContentValues cv = new ContentValues();
                change_img = AnimationUtils.loadAnimation(this,R.anim.change_list_image_anim);
                if(abandon_list.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    abandon_list.setImageResource(R.mipmap.cardview);
                    abandon_list.startAnimation(change_img);
                    LoadRecycler.cardlist(abandon_sum_delet,abandon_delete,abandon_recovey,abandon_recycler,animation,this,Lists);
                    cv.put(NotesDB.FORMAT,1);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                } else {
                    abandon_list.setImageResource(R.mipmap.listview);
                    abandon_list.startAnimation(change_img);
                    LoadRecycler.loadlist(abandon_sum_delet,abandon_delete,abandon_recovey,abandon_recycler,animation,this,Lists);
                    cv.put(NotesDB.FORMAT,0);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                }
                //修复逻辑（当处于长按状态时改变list布局样式则退出）
                if(abandon_sum_delet.getVisibility() == View.VISIBLE) {
                    abandon_sum_delet.setVisibility(View.GONE);
                    abandon_sum_delet.startAnimation(delete_down);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.activity_right_in,R.anim.activity_right_out);
        return super.onKeyDown(keyCode, event);
    }
}
