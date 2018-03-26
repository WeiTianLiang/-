package com.example.wtl.mynotes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Sumggle;
import com.example.wtl.mynotes.R;

import java.util.List;

/**
 * 便签夹适配器
 * Created by WTL on 2018/3/25.
 */

public class SumggleAdapter extends RecyclerView.Adapter<SumggleAdapter.ViewHolder>{

    private List<Sumggle> sumggleList;
    private Context context;

    public SumggleAdapter(List<Sumggle> sumggleList, Context context) {
        this.sumggleList = sumggleList;
        this.context = context;
    }

    @Override
    public SumggleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.smuggle_card,null,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SumggleAdapter.ViewHolder holder, int position) {
        Sumggle sumggle = sumggleList.get(position);
        holder.sumggle_create.setText(sumggle.getSumggle_create());
        holder.delete_number.setText(sumggle.getDelete_number());
        holder.sumggle_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sumggleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sumggle_create;
        TextView delete_number;
        LinearLayout sumggle_delete;
        View sumggle_line;

        public ViewHolder(View itemView) {
            super(itemView);
            sumggle_create = itemView.findViewById(R.id.sumggle_create);
            delete_number = itemView.findViewById(R.id.delete_number);
            sumggle_delete = itemView.findViewById(R.id.sumggle_delete);
            sumggle_line = itemView.findViewById(R.id.sumggle_line);
        }
    }
}
