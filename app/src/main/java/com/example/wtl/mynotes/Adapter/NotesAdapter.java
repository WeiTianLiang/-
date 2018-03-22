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
 * notes适配器
 * Created by WTL on 2018/3/22.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private List<Notes> list;
    private Context context;

    public NotesAdapter(List<Notes> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position) {
        Notes notes = list.get(position);
        holder.notes_content_part.setText(notes.getNotes_content_part());
        holder.notes_time.setText(notes.getNotes_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView notes_content_part;
        TextView notes_time;

        public ViewHolder(View itemView) {
            super(itemView);
            notes_content_part = (TextView) itemView.findViewById(R.id.notes_content_part);
            notes_time = (TextView) itemView.findViewById(R.id.notes_time);
        }
    }
}
