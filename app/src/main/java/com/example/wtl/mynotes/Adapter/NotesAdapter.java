package com.example.wtl.mynotes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.R;

import java.util.List;


/**
 * list布局适配器
 * Created by WTL on 2018/3/22.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private List<Notes> list;
    private Context context;

    private List<String> numlist;
    private Boolean ishow = false;

    private OnItemClickListener onItemClick;

    public NotesAdapter(List<Notes> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card,null,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.ViewHolder holder, int position) {
        Notes notes = list.get(position);
        holder.notes_content_part.setText(notes.getNotes_content_part());
        holder.notes_time.setText(notes.getNotes_time());
        //隐藏最后一个下划线
        /*if(position == list.size()-1) {
            holder.updownline.setVisibility(View.GONE);
        }*/
        if(ishow) {
            holder.check_box.setVisibility(View.VISIBLE);
        } else {
            holder.check_box.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout root_view;
        TextView notes_content_part;
        TextView notes_time;
        View updownline;
        RadioButton check_box;

        public ViewHolder(View itemView) {
            super(itemView);
            notes_content_part = itemView.findViewById(R.id.notes_content_part);
            notes_time = itemView.findViewById(R.id.notes_time);
            updownline = itemView.findViewById(R.id.updownline);
            check_box = itemView.findViewById(R.id.check_box);
            root_view = itemView.findViewById(R.id.root_view);
        }
    }

    //点击接口
    public interface OnItemClickListener {
        void OnItemClick(boolean isCheck,int postion);
        void OnRadioButtonClick(boolean isCheck,int postion);
        boolean OnItemLongClick(int poetion);
    }

    //点击事件
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClick = listener;
    }
}
