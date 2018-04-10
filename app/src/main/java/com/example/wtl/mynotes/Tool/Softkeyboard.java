package com.example.wtl.mynotes.Tool;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 控制软键盘
 * Created by WTL on 2018/4/9.
 */

public class Softkeyboard {

    //隐藏软键盘
    public static void HideSoftkeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),0);
    }

    //显示软键盘
    public static void ShowSoftkeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
    }

}
