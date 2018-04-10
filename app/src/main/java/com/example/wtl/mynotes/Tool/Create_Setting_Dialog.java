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
 * 字体大小对话框
 * Created by WTL on 2018/4/10.
 */

public class Create_Setting_Dialog extends Dialog{

    private OnLitterClickListener litterClickListener;
    private OnOrdinaryClickListener onOrdinaryClickListener;
    private OnLargeClickListener largeClickListener;

    private TextView dialog_litter;
    private TextView dialog_ordinary;
    private TextView dialog_large;

    public Create_Setting_Dialog(@NonNull Context context) {
        super(context, R.style.settingdialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_setting_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Montior();
        initEven();
    }

    private void Montior() {
        dialog_litter = findViewById(R.id.dialog_litter);
        dialog_ordinary = findViewById(R.id.dialog_ordinary);
        dialog_large = findViewById(R.id.dialog_large);
    }

    private void initEven() {
        dialog_litter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                litterClickListener.onLitterClick(dialog_litter.getText().toString());
            }
        });
        dialog_ordinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrdinaryClickListener.onOrdinaryClick(dialog_ordinary.getText().toString());
            }
        });
        dialog_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                largeClickListener.onLargeClick(dialog_large.getText().toString());
            }
        });
    }

    public interface OnLitterClickListener {
        void onLitterClick(String litter);
    }

    public interface OnOrdinaryClickListener {
        void onOrdinaryClick(String ordinary);
    }

    public interface OnLargeClickListener {
        void onLargeClick(String large);
    }

    public void setOnLitterClickListener(OnLitterClickListener litterClickListener) {
        this.litterClickListener = litterClickListener;
    }

    public void setOnOrdinaryClickListener(OnOrdinaryClickListener onOrdinaryClickListener) {
        this.onOrdinaryClickListener = onOrdinaryClickListener;
    }

    public void setOnLargeClickListener(OnLargeClickListener largeClickListener) {
        this.largeClickListener = largeClickListener;
    }

}
