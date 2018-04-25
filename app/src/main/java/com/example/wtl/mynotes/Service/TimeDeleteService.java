package com.example.wtl.mynotes.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wtl.mynotes.DB.NotesDB;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时删除服务
 * Created by WTL on 2018/4/25.
 * */

public class TimeDeleteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Long> list = new ArrayList<>();
        try {
            list = getChangeTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
        * 初始化系统时钟AlarmManager
        * */
        AlarmManager mamager = (AlarmManager) getSystemService(ALARM_SERVICE);
        /*
        * 后台每次更新间隔2s
        * */
        int clock = 2000;
        /*
        * 删除时间为7天
        * */
        int timedelay = 7*24*60*1000;
        /*
        * 循环寻找到删除时间的信息
        * */
        for(long s:list) {
            try {
                if((getTime()-s) >= timedelay) {
                    deleteTime(changeTime(s));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        /*
        * 设置系统时间
        * */
        long triggerAtTime = SystemClock.elapsedRealtime() + clock;
        /*
        * 发送广播
        * */
        Intent i = new Intent(this, TimeDeleteService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mamager.cancel(pi);
        /*
        * 设置时间的状态为睡眠时依然计时
        * */
        mamager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
    * 从数据库获取待删除的数据的时间
    * */
    private List<Long> getChangeTime() throws ParseException {
        String time;
        List<Long> longlist = new ArrayList<>();
        NotesDB notesdb = new NotesDB(this);
        SQLiteDatabase database = notesdb.getWritableDatabase();
        Cursor cursor = database.query(NotesDB.DELETE_NAME,null,null,null,null,null,null);
        if(cursor.moveToLast()) {
            do{
                time = cursor.getString(cursor.getColumnIndex("delete_time"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                long time1 = format.parse(time).getTime();
                longlist.add(time1);
            }while (cursor.moveToPrevious());
        }
        return longlist;
    }

    /*
    * 删除到时间的数据
    * */
    private void deleteTime(String time) {
        NotesDB notesdb = new NotesDB(this);
        SQLiteDatabase database = notesdb.getWritableDatabase();
        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{time});
    }

    /*
    * 获取当前Long型时间
    * */
    private Long getTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        Long time1 = format.parse(time).getTime();
        return time1;
    }

    /*
    * 将Long型时间转化为String
    * */
    private String changeTime(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String time1 = format.format(time);
        return time1;
    }

}
