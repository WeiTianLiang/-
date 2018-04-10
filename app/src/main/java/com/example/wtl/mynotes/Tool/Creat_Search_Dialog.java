package com.example.wtl.mynotes.Tool;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * 搜索dialog
 * Created by WTL on 2018/4/8.
 */

public class Creat_Search_Dialog extends Dialog {

    private ImageView back_dialog;
    private EditText search_dialog;
    private RecyclerView recycler_dialog;

    private OnBackClickListener backClickListener;
    private Context context;
    private EditText editText;
    private Animation search_visib;

    private List<Notes> stringList;
    private NotesDB notesDB;
    private SQLiteDatabase database;

    public Creat_Search_Dialog(@NonNull Context context, EditText editText) {
        super(context, R.style.searchdialog);
        this.context = context;
        this.editText = editText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Montior();
        initEven();
        search_visib = AnimationUtils.loadAnimation(context, R.anim.search_visib);
        notesDB = new NotesDB(context);
        database = notesDB.getWritableDatabase();
        search_dialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringList = new ArrayList<>();
                if (!search_dialog.getText().toString().equals("")) {
                    recycler_dialog.setVisibility(View.VISIBLE);
                    ReadCuesor.SearchFromSQL(charSequence.toString(), context, database, stringList, recycler_dialog);
                } else {
                    recycler_dialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void Montior() {
        back_dialog = findViewById(R.id.back_dialog);
        search_dialog = findViewById(R.id.search_dialog);
        recycler_dialog = findViewById(R.id.recycler_dialog);
    }

    private void initEven() {
        back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backClickListener.onbackclick();
            }
        });
    }

    public interface OnBackClickListener {
        void onbackclick();
    }

    public void setBackClickListener(OnBackClickListener backClickListener) {
        this.backClickListener = backClickListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        editText.setVisibility(View.VISIBLE);
        editText.setAnimation(search_visib);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        editText.setVisibility(View.VISIBLE);
        editText.setAnimation(search_visib);
        return super.onTouchEvent(event);
    }

}
