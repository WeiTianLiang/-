package com.example.wtl.mynotes.Tool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wtl.mynotes.R;

/**
 * 创建删除的弹出框
 * Created by WTL on 2018/4/1.
 */

public class Create_Delete_Dialog extends Dialog {
    private TextView dialog_delete_cancel;
    private TextView dialog_delete_sure;

    private TextView dia1;
    private TextView dia2;
    private TextView dia3;

    private OnTureClickListener onTureClickListener;
    private OnCancelClickListener onCancelClickListener;

    public Create_Delete_Dialog(@NonNull Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_delete_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Montior();
        initEven();
    }

    private void Montior() {
        dialog_delete_cancel = findViewById(R.id.dialog_delete_cancel);
        dialog_delete_sure = findViewById(R.id.dialog_delete_sure);
        dia1 = findViewById(R.id.dia1);
        dia2 = findViewById(R.id.dia2);
        dia3 = findViewById(R.id.dia3);
    }

    public void changetext(String tex1,String tex2,String tex3) {
        dia1.setText(tex1);
        dia2.setText(tex2);
        dia3.setText(tex3);
    }

    private void initEven() {
        dialog_delete_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClickListener.oncancelClick();
            }
        });
        dialog_delete_sure.setOnClickListener(new View.OnClickListener() {
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
