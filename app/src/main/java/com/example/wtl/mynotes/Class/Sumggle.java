package com.example.wtl.mynotes.Class;

/**
 * 便签加类
 * Created by WTL on 2018/3/25.
 */

public class Sumggle {

    private String sumggle_create;
    private String delete_number;

    public Sumggle(String sumggle_create,String delete_number) {
        this.sumggle_create = sumggle_create;
        this.delete_number = delete_number;
    }

    public String getDelete_number() {
        return delete_number;
    }

    public String getSumggle_create() {
        return sumggle_create;
    }
}
