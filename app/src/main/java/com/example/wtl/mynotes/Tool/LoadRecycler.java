package com.example.wtl.mynotes.Tool;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wtl.mynotes.Activity.EditNoteActivity;
import com.example.wtl.mynotes.Adapter.GridAdapter;
import com.example.wtl.mynotes.Adapter.NotesAdapter;
import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.DB.NotesDB;
import com.example.wtl.mynotes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 加载布局
 * Created by WTL on 2018/3/27.
 */

public class LoadRecycler {

    /*
    * 加载list布局
    * */
    public static void loadlist(final String post, String CrUp, final Notes notes, final List<String> color, final int state, final FloatingActionButton button, final LinearLayout delete, final RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具

        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.delete_floar);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.delete_down);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(150);
        animator.setAddDuration(150);
        animator.setChangeDuration(150);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        final NotesAdapter adapter = new NotesAdapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView, 0);
        /*
        * 添加新数据
        * */
        if(notes!=null && CrUp.equals("create")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.add(notes);
                    recyclerView.scrollToPosition(0);
                }
            }, 400);
        }
        /*
        * 更新数据
        * */
        if(notes!=null && CrUp.equals("update")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.update(notes,Integer.parseInt(post));
                }
            }, 400);
        }
        /*
        * 单项点击事件
        * */
        adapter.setOnItemClickAloneListener(new NotesAdapter.OnItemClickAloneListener() {
            @Override
            public void OnItemAlone(Notes notes, int pos, List<Notes> list1) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                /*
                * 根据时间传出当前点击的位置
                * */
                intent.putExtra("Postion", notes.getNotes_time());
                /*
                * 根据list的节点传出当前点击的位置
                * */
                intent.putExtra("post",String.valueOf(pos));
                /*
                * 传出当前是修改还是新建
                * */
                intent.putExtra("State", "change");
                /*
                * 传出返回后界面的位置
                * */
                intent.putExtra("back",state+"");
                /*
                * 传出背景色
                * */
                intent.putExtra("color", color.get(pos));
                context.startActivity(intent);
            }
        });
        /*
        * 长按事件
        * */
        adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                delete.startAnimation(animation1);
                button.setVisibility(View.GONE);
                /*
                * 长按事件中的单选事件
                * */
                adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int x, boolean adro, List<Notes> list1) {
                        for (int i = 0; i < list1.size(); i++) {
                            notesList.add(list1.get(i));
                        }
                        if (adro) {
                            stringList.add(x);//如果类型为true，则添加
                        } else {
                            //否则，删除当前list中的选中值
                            for (int i = 0; i < stringList.size(); i++) {
                                if (stringList.get(i) == x) {
                                    stringList.remove(i);
                                }
                            }
                        }
                        Collections.sort(stringList);//从小到大对list排序
                    }

                });
                /*
                * 删除事件
                * */
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String delete_time = null;
                        String delete_content = null;
                        String delete_color = null;
                        String delete_state_text = null;

                        for (int i = 0; i < stringList.size(); i++) {
                            if (i == 0) adapter.removeNotes(stringList.get(i));
                            else adapter.removeNotes(stringList.get(i) - i);
                            //根据时间从表1中取出数据
                            String sql = "select * from notes where time='" + notesList.get(stringList.get(i)).getNotes_time() + "'";
                            Cursor cursor = database.rawQuery(sql, null);
                            if (cursor.moveToFirst()) {
                                delete_time = cursor.getString(cursor.getColumnIndex("time"));
                                delete_content = cursor.getString(cursor.getColumnIndex("content"));
                                delete_color = cursor.getString(cursor.getColumnIndex("color"));
                                delete_state_text = cursor.getString(cursor.getColumnIndex("statetext"));
                            }
                            //根据时间删除表中数据
                            database.delete(NotesDB.TABLE_NAME, NotesDB.TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                            //把数据插入表4
                            ContentValues cv = new ContentValues();
                            cv.put(NotesDB.DELETE_TIME, delete_time);
                            cv.put(NotesDB.DELETE_CONTENT, delete_content);
                            cv.put(NotesDB.DELETE_COLOR, delete_color);
                            cv.put(NotesDB.DELETE_STATETEXT, delete_state_text);
                            database.insert(NotesDB.DELETE_NAME, null, cv);
                        }
                        stringList.removeAll(stringList);//清空位置表
                        notesList.removeAll(notesList);//清空数据表
                        delete.setVisibility(View.GONE);
                        delete.startAnimation(animation2);
                        button.setVisibility(View.VISIBLE);
                        /*
                        * 线程中延迟470执行，否则无法显示动画
                        * */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.isLongItem();
                            }
                        },470);
                    }
                });
                return true;
            }
        });
    }

    /*
    * 加载card布局
    * */
    public static void cardlist(final String post,String CrUp,final Notes notes, final List<String> color, final int state, final FloatingActionButton button, final LinearLayout delete, final RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具

        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.delete_floar);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.delete_down);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(150);
        animator.setChangeDuration(150);
        animator.setAddDuration(150);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        final GridAdapter adapter = new GridAdapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView, 1);

        /*
        * 添加新数据
        * */
        if(notes!=null && CrUp.equals("create")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.add(notes);
                    recyclerView.scrollToPosition(0);
                }
            }, 400);
        }
        /*
        * 更新数据
        * */
        if(notes!=null && CrUp.equals("update")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.update(notes,Integer.parseInt(post));
                }
            }, 400);
        }
        adapter.setOnItemClickAloneListener(new GridAdapter.OnItemClickAloneListener() {
            @Override
            public void OnItemAlone(Notes notes, int pos, List<Notes> list1) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("Postion", notes.getNotes_time());
                intent.putExtra("post",String.valueOf(pos));
                intent.putExtra("State", "change");
                intent.putExtra("back",state+"");
                intent.putExtra("color",color.get(pos));
                context.startActivity(intent);
                /*((Activity) context).finish();*///因为context没有finish操作,将context强转为activity
            }
        });

        adapter.setOnItemLongClickListener(new GridAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                delete.setAnimation(animation1);
                button.setVisibility(View.GONE);

                adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int x, boolean adro, List<Notes> list) {
                        for (int i = 0; i < list.size(); i++) {
                            notesList.add(list.get(i));
                        }
                        if (adro) {
                            stringList.add(x);//如果类型为true，则添加
                        } else {
                            //否则，删除当前list中的选中值
                            for (int i = 0; i < stringList.size(); i++) {
                                if (stringList.get(i) == x) {
                                    stringList.remove(i);
                                }
                            }
                        }
                        Collections.sort(stringList);//从小到大对list排序
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String delete_time = null;
                        String delete_content = null;
                        String delete_color = null;
                        String delete_state_text = null;

                        for (int i = 0; i < stringList.size(); i++) {
                            if (i == 0) adapter.removeNotes(stringList.get(i));
                            else adapter.removeNotes(stringList.get(i) - i);
                            //根据时间从表1中取出数据
                            String sql = "select * from notes where time='" + notesList.get(stringList.get(i)).getNotes_time() + "'";
                            Cursor cursor = database.rawQuery(sql, null);
                            if (cursor.moveToFirst()) {
                                delete_time = cursor.getString(cursor.getColumnIndex("time"));
                                delete_content = cursor.getString(cursor.getColumnIndex("content"));
                                delete_color = cursor.getString(cursor.getColumnIndex("color"));
                                delete_state_text = cursor.getString(cursor.getColumnIndex("statetext"));
                            }
                            //根据时间从表1中删除数据
                            database.delete(NotesDB.TABLE_NAME, NotesDB.TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                            //把数据插入表4
                            ContentValues cv = new ContentValues();
                            cv.put(NotesDB.DELETE_TIME, delete_time);
                            cv.put(NotesDB.DELETE_CONTENT, delete_content);
                            cv.put(NotesDB.DELETE_COLOR, delete_color);
                            cv.put(NotesDB.DELETE_STATETEXT, delete_state_text);
                            database.insert(NotesDB.DELETE_NAME, null, cv);
                        }
                        stringList.removeAll(stringList);//清空表
                        notesList.removeAll(notesList);//清空数据表
                        delete.setVisibility(View.GONE);
                        delete.setAnimation(animation2);
                        button.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.isLongItem();
                            }
                        },470);
                    }
                });
                return true;
            }
        });
    }

    /*
    * 加载list布局--删除
    * */
    public static void loadlist(final LinearLayout delete, final ImageView abandon_dele, final ImageView abandon_move, RecyclerView recyclerView, Animation animation, final Context context, final List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具

        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.delete_floar);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.delete_down);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(150);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        final NotesAdapter adapter = new NotesAdapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView, 0);

        /*
        * 单个恢复
        * */
        adapter.setOnItemClickAloneListener(new NotesAdapter.OnItemClickAloneListener() {
            String recoy_time;
            String recoy_content;
            String recoy_color;
            String recoy_state_num;
            String recoy_state_text;

            @Override
            public void OnItemAlone(Notes notes, final int pos, final List<Notes> list1) {
                final List<Notes> notelist = new ArrayList<>();
                for(int i = 0 ; i < list1.size() ; i++) {
                    notelist.add(list1.get(i));
                }
                final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                createDeleteDialog.setCanceledOnTouchOutside(false);
                createDeleteDialog.show();
                createDeleteDialog.changetext("垃圾堆中无法对文本进行操作", "请先恢复该文本", null);
                createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                    @Override
                    public void oncancelClick() {
                        createDeleteDialog.dismiss();
                    }
                });
                createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                    @Override
                    public void ontureClick() {
                        createDeleteDialog.dismiss();
                        adapter.removeNotes(pos);
                        //根据时间从表4中取出数据
                        String sql = "select * from deleted where delete_time='" + notelist.get(pos).getNotes_time() + "'";
                        Cursor cursor = database.rawQuery(sql, null);
                        if (cursor.moveToFirst()) {
                            recoy_time = cursor.getString(cursor.getColumnIndex("delete_time"));
                            recoy_content = cursor.getString(cursor.getColumnIndex("delete_content"));
                            recoy_color = cursor.getString(cursor.getColumnIndex("delete_color"));
                            recoy_state_num = cursor.getString(cursor.getColumnIndex("delete_statenum"));
                            recoy_state_text = cursor.getString(cursor.getColumnIndex("delete_statetext"));
                        }
                        //根据时间删除表中数据
                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notelist.get(pos).getNotes_time()});
                        Toast.makeText(context, "数据已被恢复!!", Toast.LENGTH_SHORT).show();
                        //把数据恢复到表1
                        ContentValues cv = new ContentValues();
                        cv.put(NotesDB.TIME, recoy_time);
                        cv.put(NotesDB.CONTENT, recoy_content);
                        cv.put(NotesDB.COLOR,recoy_color);
                        cv.put(NotesDB.STATETEXT,recoy_state_text);
                        database.insert(NotesDB.TABLE_NAME, null, cv);
                        Intent intent = new Intent("com.example.wtl.mynotes.action");
                        intent.putExtra("recoy","yes");
                        context.sendBroadcast(intent);
                    }
                });
            }
        });

        adapter.setOnItemLongClickListener(new NotesAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                delete.startAnimation(animation1);

                adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int x, boolean adro, List<Notes> list1) {
                        for (int i = 0; i < list1.size(); i++) {
                            notesList.add(list1.get(i));
                        }
                        if (adro) {
                            stringList.add(x);//如果类型为true，则添加
                        } else {
                            //否则，删除当前list中的选中值
                            for (int i = 0; i < stringList.size(); i++) {
                                if (stringList.get(i) == x) {
                                    stringList.remove(i);
                                }
                            }
                        }
                        Collections.sort(stringList);//从小到大对list排序
                    }

                });
                //监听删除
                abandon_dele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stringList.size() == 0) {
                            delete.setVisibility(View.GONE);
                            delete.startAnimation(animation2);
                            /*
                            * 如果未选中则直接调用isLongItem不用延迟
                            * */
                            adapter.isLongItem();
                        } else {
                            final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                            createDeleteDialog.setCanceledOnTouchOutside(false);
                            createDeleteDialog.show();
                            Window window = createDeleteDialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                                @Override
                                public void oncancelClick() {
                                    createDeleteDialog.dismiss();
                                }
                            });
                            createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                                @Override
                                public void ontureClick() {
                                    createDeleteDialog.dismiss();
                                    for (int i = 0; i < stringList.size(); i++) {
                                        if (i == 0) adapter.removeNotes(stringList.get(i));
                                        else adapter.removeNotes(stringList.get(i) - i);
                                        //根据时间删除表中数据
                                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                                    }
                                    stringList.removeAll(stringList);//清空表
                                    delete.setVisibility(View.GONE);
                                    delete.startAnimation(animation2);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.isLongItem();
                                        }
                                    },470);
                                }
                            });
                        }
                    }
                });
                //监听恢复
                abandon_move.setOnClickListener(new View.OnClickListener() {
                    String recoy_time;
                    String recoy_content;
                    String recoy_color;
                    String recoy_state_text;

                    @Override
                    public void onClick(View view) {
                        if (stringList.size() == 0) {
                            delete.setVisibility(View.GONE);
                            delete.startAnimation(animation2);
                            adapter.isLongItem();
                        } else {
                            final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                            createDeleteDialog.setCanceledOnTouchOutside(false);
                            createDeleteDialog.show();
                            createDeleteDialog.changetext("此项操作执行后", "您的数据将被恢复", "Are You Ready?");
                            Window window = createDeleteDialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                                @Override
                                public void oncancelClick() {
                                    createDeleteDialog.dismiss();
                                }
                            });
                            createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                                @Override
                                public void ontureClick() {
                                    createDeleteDialog.dismiss();
                                    for (int i = 0; i < stringList.size(); i++) {
                                        if (i == 0) adapter.removeNotes(stringList.get(i));
                                        else adapter.removeNotes(stringList.get(i) - i);
                                        //根据时间从表4中取出数据
                                        String sql = "select * from deleted where delete_time='" + notesList.get(stringList.get(i)).getNotes_time() + "'";
                                        Cursor cursor = database.rawQuery(sql, null);
                                        if (cursor.moveToFirst()) {
                                            recoy_time = cursor.getString(cursor.getColumnIndex("delete_time"));
                                            recoy_content = cursor.getString(cursor.getColumnIndex("delete_content"));
                                            recoy_color = cursor.getString(cursor.getColumnIndex("delete_color"));
                                            recoy_state_text = cursor.getString(cursor.getColumnIndex("delete_statetext"));
                                        }
                                        //根据时间删除表中数据
                                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                                        //把数据恢复到表1
                                        ContentValues cv = new ContentValues();
                                        cv.put(NotesDB.TIME, recoy_time);
                                        cv.put(NotesDB.CONTENT, recoy_content);
                                        cv.put(NotesDB.COLOR, recoy_color);
                                        cv.put(NotesDB.STATETEXT,recoy_state_text);
                                        database.insert(NotesDB.TABLE_NAME, null, cv);
                                    }
                                    Toast.makeText(context, "数据已被恢复!!", Toast.LENGTH_SHORT).show();
                                    stringList.removeAll(stringList);//清空表
                                    delete.setVisibility(View.GONE);
                                    delete.startAnimation(animation2);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.isLongItem();
                                        }
                                    },470);
                                    Intent intent = new Intent("com.example.wtl.mynotes.action");
                                    intent.putExtra("recoy","yes");
                                    context.sendBroadcast(intent);
                                }
                            });
                        }
                    }
                });
                return true;
            }
        });
    }

    /*
    * 加载card布局--删除
    * */
    public static void cardlist(final LinearLayout delete, final ImageView abandon_dele, final ImageView abandon_move, RecyclerView recyclerView, Animation animation, final Context context, List<Notes> list) {
        final List<Integer> stringList = new ArrayList<>();//定义list存储要删除的数
        final List<Notes> notesList = new ArrayList<>();//定义list存储适配器传来的值
        NotesDB notesDB = new NotesDB(context);//初始化数据库
        final SQLiteDatabase database = notesDB.getWritableDatabase();//初始化数据库操作工具

        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.delete_floar);
        final Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.delete_down);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(200);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        final GridAdapter adapter = new GridAdapter(list, context);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView, 1);

        adapter.setOnItemClickAloneListener(new GridAdapter.OnItemClickAloneListener() {
            String recoy_time;
            String recoy_content;
            String recoy_color;
            String recoy_state_text;

            @Override
            public void OnItemAlone(Notes notes, final int pos, List<Notes> list1) {
                final List<Notes> notelist = new ArrayList<>();
                for(int i = 0 ; i < list1.size() ; i++) {
                    notelist.add(list1.get(i));
                }
                final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                createDeleteDialog.setCanceledOnTouchOutside(false);
                createDeleteDialog.show();
                createDeleteDialog.changetext("垃圾堆中无法对文本进行操作", "请先恢复该文本", null);
                createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                    @Override
                    public void oncancelClick() {
                        createDeleteDialog.dismiss();
                    }
                });
                createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                    @Override
                    public void ontureClick() {
                        createDeleteDialog.dismiss();
                        adapter.removeNotes(pos);
                        //根据时间从表4中取出数据
                        String sql = "select * from deleted where delete_time='" + notelist.get(pos).getNotes_time() + "'";
                        Cursor cursor = database.rawQuery(sql, null);
                        if (cursor.moveToFirst()) {
                            recoy_time = cursor.getString(cursor.getColumnIndex("delete_time"));
                            recoy_content = cursor.getString(cursor.getColumnIndex("delete_content"));
                            recoy_color = cursor.getString(cursor.getColumnIndex("delete_color"));
                            recoy_state_text = cursor.getString(cursor.getColumnIndex("delete_statetext"));
                        }
                        //根据时间删除表中数据
                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notelist.get(pos).getNotes_time()});
                        Toast.makeText(context, "数据已被恢复!!", Toast.LENGTH_SHORT).show();
                        //把数据恢复到表1
                        ContentValues cv = new ContentValues();
                        cv.put(NotesDB.TIME, recoy_time);
                        cv.put(NotesDB.CONTENT, recoy_content);
                        cv.put(NotesDB.COLOR, recoy_color);
                        cv.put(NotesDB.STATETEXT,recoy_state_text);
                        database.insert(NotesDB.TABLE_NAME, null, cv);
                        Intent intent = new Intent("com.example.wtl.mynotes.action");
                        intent.putExtra("recoy","yes");
                        context.sendBroadcast(intent);
                    }
                });
            }
        });

        adapter.setOnItemLongClickListener(new GridAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick() {
                adapter.notifyDataSetChanged();
                delete.setVisibility(View.VISIBLE);
                delete.setAnimation(animation1);

                adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int x, boolean adro, List<Notes> list) {
                        for (int i = 0; i < list.size(); i++) {
                            notesList.add(list.get(i));
                        }
                        if (adro) {
                            stringList.add(x);//如果类型为true，则添加
                        } else {
                            //否则，删除当前list中的选中值
                            for (int i = 0; i < stringList.size(); i++) {
                                if (stringList.get(i) == x) {
                                    stringList.remove(i);
                                }
                            }
                        }
                        Collections.sort(stringList);//从小到大对list排序
                    }
                });

                abandon_dele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stringList.size() == 0) {
                            delete.setVisibility(View.GONE);
                            delete.setAnimation(animation2);
                            adapter.isLongItem();
                        } else {
                            final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                            createDeleteDialog.setCanceledOnTouchOutside(false);
                            createDeleteDialog.show();
                            Window window = createDeleteDialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                                @Override
                                public void oncancelClick() {
                                    createDeleteDialog.dismiss();
                                }
                            });
                            createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                                @Override
                                public void ontureClick() {
                                    createDeleteDialog.dismiss();
                                    for (int i = 0; i < stringList.size(); i++) {
                                        if (i == 0) adapter.removeNotes(stringList.get(i));
                                        else adapter.removeNotes(stringList.get(i) - i);
                                        //根据时间从表1中删除数据
                                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                                    }
                                    stringList.removeAll(stringList);//清空表
                                    delete.setVisibility(View.GONE);
                                    delete.setAnimation(animation2);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.isLongItem();
                                        }
                                    },470);
                                }
                            });
                        }
                    }
                });
                abandon_move.setOnClickListener(new View.OnClickListener() {
                    String recoy_time;
                    String recoy_content;
                    String recoy_color;
                    String recoy_state_text;

                    @Override
                    public void onClick(View view) {
                        if (stringList.size() == 0) {
                            delete.setVisibility(View.GONE);
                            delete.startAnimation(animation2);
                            adapter.isLongItem();
                        } else {
                            final Create_Delete_Dialog createDeleteDialog = new Create_Delete_Dialog(context);
                            createDeleteDialog.setCanceledOnTouchOutside(false);
                            createDeleteDialog.show();
                            createDeleteDialog.changetext("此项操作执行后", "您的数据将被恢复", "Are You Ready?");
                            Window window = createDeleteDialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            createDeleteDialog.setOnCancelClickListener(new Create_Delete_Dialog.OnCancelClickListener() {
                                @Override
                                public void oncancelClick() {
                                    createDeleteDialog.dismiss();
                                }
                            });
                            createDeleteDialog.setOnTureClickListener(new Create_Delete_Dialog.OnTureClickListener() {
                                @Override
                                public void ontureClick() {
                                    createDeleteDialog.dismiss();
                                    for (int i = 0; i < stringList.size(); i++) {
                                        if (i == 0) adapter.removeNotes(stringList.get(i));
                                        else adapter.removeNotes(stringList.get(i) - i);
                                        //根据时间从表4中取出数据
                                        String sql = "select * from deleted where delete_time='" + notesList.get(stringList.get(i)).getNotes_time() + "'";
                                        Cursor cursor = database.rawQuery(sql, null);
                                        if (cursor.moveToFirst()) {
                                            recoy_time = cursor.getString(cursor.getColumnIndex("delete_time"));
                                            recoy_content = cursor.getString(cursor.getColumnIndex("delete_content"));
                                            recoy_color = cursor.getString(cursor.getColumnIndex("delete_color"));
                                            recoy_state_text = cursor.getString(cursor.getColumnIndex("delete_statetext"));
                                        }
                                        //根据时间删除表中数据
                                        database.delete(NotesDB.DELETE_NAME, NotesDB.DELETE_TIME + "= ?", new String[]{notesList.get(stringList.get(i)).getNotes_time()});
                                        Toast.makeText(context, "数据已被恢复!!", Toast.LENGTH_SHORT).show();
                                        //把数据恢复到表1
                                        ContentValues cv = new ContentValues();
                                        cv.put(NotesDB.TIME, recoy_time);
                                        cv.put(NotesDB.CONTENT, recoy_content);
                                        cv.put(NotesDB.COLOR, recoy_color);
                                        cv.put(NotesDB.STATETEXT,recoy_state_text);
                                        database.insert(NotesDB.TABLE_NAME, null, cv);
                                    }
                                    stringList.removeAll(stringList);//清空表
                                    delete.setVisibility(View.GONE);
                                    delete.startAnimation(animation2);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.isLongItem();
                                        }
                                    },470);
                                    Intent intent = new Intent("com.example.wtl.mynotes.action");
                                    intent.putExtra("recoy","yes");
                                    context.sendBroadcast(intent);
                                }
                            });
                        }
                    }
                });
                return true;
            }
        });
    }

    /*
    * 运行使用layoutAnimal加载的动画
    * */
    private static void runLayoutAnimation(final RecyclerView recyclerView, int x) {
        if (x == 0) {
            final Context context = recyclerView.getContext();
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_change_list_anim);
            recyclerView.setLayoutAnimation(controller);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        } else {
            final Context context = recyclerView.getContext();
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animal);
            recyclerView.setLayoutAnimation(controller);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
    }

}
