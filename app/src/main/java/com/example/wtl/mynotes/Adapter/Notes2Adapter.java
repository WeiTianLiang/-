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

    public Notes2Adapter(List<Notes> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Notes2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card_2,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Notes2Adapter.ViewHolder holder, int position) {
        Notes notes = list.get(position);
        holder.notes_content_part_2.setText(notes.getNotes_content_part());
        holder.notes_time_2.setText(notes.getNotes_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView notes_content_part_2;
        TextView notes_time_2;

        public ViewHolder(View itemView) {
            super(itemView);
            notes_content_part_2 = itemView.findViewById(R.id.notes_content_part_2);
            notes_time_2 = itemView.findViewById(R.id.notes_time_2);
        }

    }

}
