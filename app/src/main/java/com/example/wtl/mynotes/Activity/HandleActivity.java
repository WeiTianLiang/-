package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
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
import com.example.wtl.mynotes.Tool.ReadCuesor;

import java.util.ArrayList;
import java.util.List;

public class HandleActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView handle_recycler;
    private Animation animation;
    private Animation change_img;
    private Animation delete_down;
    private List<Notes> notesList = new ArrayList<>();
    private ImageView handle_list;
    private ImageView handle_img_back;
    private TextView handle_text_back;

    private NotesDB notesDB;
    private SQLiteDatabase readbase;

    private SharedPreferences preferences;

    private FloatingActionButton add_handle;
    private LinearLayout sum_delet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);
        HideScreenTop.HideScreenTop(getWindow());
        notesDB = new NotesDB(this);
        readbase = notesDB.getWritableDatabase();
        Montior();
        preferences = getSharedPreferences("first_act",0);
        Boolean user_first = preferences.getBoolean("FIRST",true);
        delete_down = AnimationUtils.loadAnimation(this,R.anim.delete_down);
        IsFirstOpen.IsFirstOpen(1,add_handle,sum_delet,handle_list,preferences,user_first,this,readbase,notesList,handle_recycler,animation);
    }

    private void Montior() {
        handle_recycler = (RecyclerView) findViewById(R.id.handle_recycler);
        handle_list = (ImageView) findViewById(R.id.handle_list);
        handle_img_back = (ImageView) findViewById(R.id.handle_img_back);
        handle_text_back = (TextView) findViewById(R.id.handle_text_back);
        add_handle = (FloatingActionButton) findViewById(R.id.add_handle);
        sum_delet = (LinearLayout) findViewById(R.id.sum_delet);

        handle_list.setOnClickListener(this);
        handle_img_back.setOnClickListener(this);
        handle_text_back.setOnClickListener(this);
        add_handle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_handle:
                Intent intent = new Intent(HandleActivity.this,EditNoteActivity.class);
                intent.putExtra("State","add");
                intent.putExtra("back","1");//记录返回的界面，0代表MainActivity，1代表HandleActivity
                intent.putExtra("color","white");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
                break;
            case R.id.handle_img_back:
                finish();
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                break;
            case R.id.handle_text_back:
                finish();
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                break;
            case R.id.handle_list:
                ContentValues cv = new ContentValues();
                change_img = AnimationUtils.loadAnimation(this,R.anim.change_list_image_anim);
                if(handle_list.getDrawable().getCurrent().getConstantState().
                        equals(this.getResources().getDrawable(R.mipmap.listview).getConstantState())) {
                    handle_list.setImageResource(R.mipmap.cardview);
                    handle_list.startAnimation(change_img);
                    LoadRecycler.cardlist(null,null,null,ReadCuesor.ReadColor(readbase),1,add_handle,sum_delet,handle_recycler,animation,this,notesList);
                    cv.put(NotesDB.FORMAT,1);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                } else {
                    handle_list.setImageResource(R.mipmap.listview);
                    handle_list.startAnimation(change_img);
                    LoadRecycler.loadlist(null,null,null,ReadCuesor.ReadColor(readbase),1,add_handle,sum_delet,handle_recycler,animation,this,notesList);
                    cv.put(NotesDB.FORMAT,0);
                    readbase.insert(NotesDB.FORMAT_NAME,null,cv);
                }
                //修复逻辑（当处于长按状态时改变list布局样式则退出）
                if(sum_delet.getVisibility() == View.VISIBLE) {
                    add_handle.setVisibility(View.VISIBLE);
                    sum_delet.setVisibility(View.GONE);
                    sum_delet.startAnimation(delete_down);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
        return super.onKeyDown(keyCode, event);
    }
}
