package com.example.wtl.mynotes.Tool;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;

/**
 * 延迟收放search
 * Created by WTL on 2018/4/10.
 */

public class TimerTaskSearch {

    public static void TimerTaskSearch(final View view, final Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
                view.setAnimation(animation);
            }
        },250);
    }

}
