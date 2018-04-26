package com.example.wtl.mynotes.Tool;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建新建弹出框
 * Created by WTL on 2018/3/25.
 */

public class Create_Dialog extends Dialog {

    private TextView dialog_cancel;
    private TextView dialog_sure;
    private EditText dialog_edittext;

    private OnTureClickListener onTureClickListener;
    private OnCancelClickListener onCancelClickListener;

    private NotesDB notesDB;
    private SQLiteDatabase writebase;

    public static int flag = 0;
    private List<String> stringList = new ArrayList<>();

    public Create_Dialog(@NonNull Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dialog);
        notesDB = new NotesDB(getContext());
        writebase = notesDB.getWritableDatabase();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Montior();
        initEven();
    }

    //将新的文件夹名称存入数据库
    private void write_sumggle() {
        Cursor cursor = writebase.query(NotesDB.NOTECLIP_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                stringList.add(memo);
            } while (cursor.moveToNext());
        }
        if (!dialog_edittext.getText().toString().equals("") && !stringList.contains(dialog_edittext.getText().toString())) {
            ContentValues cv = new ContentValues();
            cv.put(NotesDB.NOTECLIP_MEMO, dialog_edittext.getText().toString());
            writebase.insert(NotesDB.NOTECLIP_NAME, null, cv);
            flag = 0;
        } else if (dialog_edittext.getText().toString().equals("") && !stringList.contains(dialog_edittext.getText().toString())) {
            Toast.makeText(getContext(), "~请输入文件夹名称~", Toast.LENGTH_SHORT).show();
            flag = 1;
        } else if (!dialog_edittext.getText().toString().equals("") && stringList.contains(dialog_edittext.getText().toString())) {
            Toast.makeText(getContext(), "~该文件名已存在~", Toast.LENGTH_SHORT).show();
            flag = 1;
        }
    }

    private void Montior() {
        dialog_cancel = findViewById(R.id.dialog_cancel);
        dialog_sure = findViewById(R.id.dialog_sure);
        dialog_edittext = findViewById(R.id.dialog_edittext);
    }

    private void initEven() {
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClickListener.oncancelClick();
            }
        });
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_sumggle();
                onTureClickListener.ontureClick(dialog_edittext.getText().toString());
            }
        });
    }

    public interface OnTureClickListener {
        void ontureClick(String s);
    }

    public interface OnCancelClickListener {
        void oncancelClick();
    }

    public void setOnTureClickListener(OnTureClickListener onTureClickListener) {
        this.onTureClickListener = onTureClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
}
