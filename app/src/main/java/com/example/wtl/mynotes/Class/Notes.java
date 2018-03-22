package com.example.wtl.mynotes.Class;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * notes类
 * Created by WTL on 2018/3/22.
 */

public class Notes implements Parcelable{

    private String notes_content_part;
    private String notes_time;

    public Notes(String notes_content_part,String notes_time) {
        this.notes_content_part = notes_content_part;
        this.notes_time = notes_time;
    }

    protected Notes(Parcel in) {
        notes_content_part = in.readString();
        notes_time = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getNotes_time() {
        return notes_time;
    }

    public String getNotes_content_part() {
        return notes_content_part;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(notes_content_part);
        parcel.writeString(notes_time);
    }
}
