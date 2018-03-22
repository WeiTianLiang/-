package com.example.wtl.mynotes.Class;

/**
 * notes类
 * Created by WTL on 2018/3/22.
 */

public class Notes {

    private String notes_content_part;
    private String notes_time;

    public Notes(String notes_content_part,String notes_time) {
        this.notes_content_part = notes_content_part;
        this.notes_time = notes_time;
    }

    public String getNotes_time() {
        return notes_time;
    }

    public String getNotes_content_part() {
        return notes_content_part;
    }
}
