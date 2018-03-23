package com.example.wtl.mynotes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.R;

import java.util.List;

/**
 * card布局适配器
 * Created by WTL on 2018/3/23.
 */

public class Notes2Adapter extends RecyclerView.Adapter<Notes2Adapter.ViewHolder>{

    private List<Notes> list;
    private Context context;

    private NotesAdapter.OnItemClickListener onItemClick = null;
    private NotesAdapter.OnItemLongClickListener onItemLongClick = null;

    public Notes2Adapter(List<Notes> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Notes2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card_2,null,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Notes2Adapter.ViewHolder holder, int position) {
        Notes notes = list.get(position);
        holder.notes_content_part.setText(notes.getNotes_content_part());
        holder.notes_time.setText(notes.getNotes_time());
        if(position == list.size()-1) {
            holder.updownline.setVisibility(View.GONE);
        }
        //点击事件
        if(onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion = holder.getLayoutPosition();
                    onItemClick.OnItemClick(holder.itemView,postion);
                }
            });
        }
        //长按事件
        if(onItemLongClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int postion = holder.getLayoutPosition();
                    onItemLongClick.OnItemLongClick(holder.itemView,postion);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView notes_content_part;
        TextView notes_time;
        View updownline;

        public ViewHolder(View itemView) {
            super(itemView);
            notes_content_part = itemView.findViewById(R.id.notes_content_part);
            notes_time = itemView.findViewById(R.id.notes_time);
            updownline = itemView.findViewById(R.id.updownline);
        }

    }

    //点击接口
    public interface OnItemClickListener {
        void OnItemClick(View view,int postion);
    }
    //长按接口
    public interface OnItemLongClickListener {
        void OnItemLongClick(View view,int poetion);
    }
    //点击事件
    public void setOnItemClickListener(NotesAdapter.OnItemClickListener listener) {
        this.onItemClick = listener;
    }
    //长按事件
    public void setOnItemLongClickListener(NotesAdapter.OnItemLongClickListener listener) {
        this.onItemLongClick = listener;
    }
}
