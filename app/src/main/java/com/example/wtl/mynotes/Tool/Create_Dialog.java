package com.example.wtl.mynotes.Tool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wtl.mynotes.R;

/**
 * 创建新建弹出框
 * Created by WTL on 2018/3/25.
 */

public class Create_Dialog extends Dialog{

    private TextView dialog_cancel;
    private TextView dialog_sure;
    private EditText dialog_edittext;

    private OnTureClickListener onTureClickListener;
    private OnCancelClickListener onCancelClickListener;

    public Create_Dialog(@NonNull Context context) {
        super(context,R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dialog);
        Montior();
        initEven();
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
                onTureClickListener.ontureClick();
            }
        });
    }

    public interface OnTureClickListener {
        void ontureClick();
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
