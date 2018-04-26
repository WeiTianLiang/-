package com.example.wtl.mynotes.Tool;

import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机产生背景色
 * Created by WTL on 2018/4/26.
 */

public class RandomColor {

    private static List<Integer> color = new ArrayList<>();

    public static int RandomColor() {
        color.add(R.color.random0);
        color.add(R.color.random1);
        color.add(R.color.random3);
        color.add(R.color.random4);
        color.add(R.color.random5);
        color.add(R.color.random6);
        color.add(R.color.random7);
        color.add(R.color.random8);
        color.add(R.color.random9);
        color.add(R.color.random10);
        color.add(R.color.random11);
        color.add(R.color.random12);
        int x = (int)(Math.random()*color.size());
        return color.get(x);
    }

}
