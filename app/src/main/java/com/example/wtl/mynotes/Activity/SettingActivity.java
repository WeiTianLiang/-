package com.example.wtl.mynotes.Activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;
import com.example.wtl.mynotes.Tool.Create_Setting_Dialog;
import com.example.wtl.mynotes.Tool.HideScreenTop;
import com.example.wtl.mynotes.Tool.IsFirstOpen;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView setting_text_back;
    private FrameLayout word_size;
    private TextView word_size_show;

    private NotesDB notesDB;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Montior();
        HideScreenTop.HideScreenTop(getWindow());
        notesDB = new NotesDB(this);
        database = notesDB.getWritableDatabase();
        IsFirstOpen.IsFirstOpenWord(word_size_show,database);
    }

    private void Montior() {
        setting_text_back = (TextView) findViewById(R.id.setting_text_back);
        word_size = (FrameLayout) findViewById(R.id.word_size);
        word_size_show = (TextView) findViewById(R.id.word_size_show);

        setting_text_back.setOnClickListener(this);
        word_size.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.setting_text_back:
                finish();
                overridePendingTransition(R.anim.activity_right_out,R.anim.activity_right_in);
                break;
            case R.id.word_size:
                final Create_Setting_Dialog setting_dialog = new Create_Setting_Dialog(SettingActivity.this);
                setting_dialog.show();
                Window window = setting_dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                final ContentValues cv = new ContentValues();
                setting_dialog.setOnLitterClickListener(new Create_Setting_Dialog.OnLitterClickListener() {
                    @Override
                    public void onLitterClick(String litter) {
                        setting_dialog.dismiss();
                        word_size_show.setText(litter);
                        cv.put(NotesDB.WORD_SIZE,"litter");
                        database.insert(NotesDB.WORD_NAME,null,cv);
                    }
                });
                setting_dialog.setOnOrdinaryClickListener(new Create_Setting_Dialog.OnOrdinaryClickListener() {
                    @Override
                    public void onOrdinaryClick(String ordinary) {
                        setting_dialog.dismiss();
                        word_size_show.setText(ordinary);
                        cv.put(NotesDB.WORD_SIZE,"ordinary");
                        database.insert(NotesDB.WORD_NAME,null,cv);
                    }
                });
                setting_dialog.setOnLargeClickListener(new Create_Setting_Dialog.OnLargeClickListener() {
                    @Override
                    public void onLargeClick(String large) {
                        setting_dialog.dismiss();
                        word_size_show.setText(large);
                        cv.put(NotesDB.WORD_SIZE,"large");
                        database.insert(NotesDB.WORD_NAME,null,cv);
                    }
                });
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
