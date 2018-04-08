package com.example.wtl.mynotes.Tool;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.R;

/**
 * 更换背景色
 * Created by WTL on 2018/4/3.
 */

public class Change_Colors {

    public static void Change_Colors(Context context, String colors, LinearLayout layout_colors, EditText text_colors, LinearLayout top_colors, TextView time,LinearLayout bottom) {
        switch(colors) {
            case "white":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.white));
                text_colors.setBackground(context.getResources().getDrawable(R.color.white));
                text_colors.setTextColor(context.getResources().getColor(R.color.whitetext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.white));
                time.setTextColor(context.getResources().getColor(R.color.whitetext));
                bottom.setBackground(context.getResources().getDrawable(R.color.white));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.white);
                break;
            case "red":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.red));
                text_colors.setBackground(context.getResources().getDrawable(R.color.red));
                text_colors.setTextColor(context.getResources().getColor(R.color.redtext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.red));
                time.setTextColor(context.getResources().getColor(R.color.redtext));
                bottom.setBackground(context.getResources().getDrawable(R.color.red));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.red);
                break;
            case "green":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.green));
                text_colors.setBackground(context.getResources().getDrawable(R.color.green));
                text_colors.setTextColor(context.getResources().getColor(R.color.greentext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.green));
                time.setTextColor(context.getResources().getColor(R.color.greentext));
                bottom.setBackground(context.getResources().getDrawable(R.color.green));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.green);
                break;
            case "blue":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.blue));
                text_colors.setBackground(context.getResources().getDrawable(R.color.blue));
                text_colors.setTextColor(context.getResources().getColor(R.color.bluetext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.blue));
                time.setTextColor(context.getResources().getColor(R.color.bluetext));
                bottom.setBackground(context.getResources().getDrawable(R.color.blue));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.blue);
                break;
            case "violet":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.violet));
                text_colors.setBackground(context.getResources().getDrawable(R.color.violet));
                text_colors.setTextColor(context.getResources().getColor(R.color.violettext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.violet));
                time.setTextColor(context.getResources().getColor(R.color.violettext));
                bottom.setBackground(context.getResources().getDrawable(R.color.violet));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.violet);
                break;
            case "black":
                layout_colors.setBackground(context.getResources().getDrawable(R.color.black));
                text_colors.setBackground(context.getResources().getDrawable(R.color.black));
                text_colors.setTextColor(context.getResources().getColor(R.color.blacktext));
                top_colors.setBackground(context.getResources().getDrawable(R.color.black));
                time.setTextColor(context.getResources().getColor(R.color.blacktext));
                bottom.setBackground(context.getResources().getDrawable(R.color.black));
                StatusBarUtils.setWindowStatusBarColor((Activity) context,R.color.black);
                break;
        }
    }

}
