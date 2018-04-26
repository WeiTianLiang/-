package com.example.wtl.mynotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Activity.CstomSumActivity;
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
    public void onBindViewHolder(SumggleAdapter.ViewHolder holder, final int position) {
        final Sumggle sumggle = sumggleList.get(position);
        holder.sumggle_create.setText(sumggle.getSumggle_create());
        holder.delete_number.setText(sumggle.getDelete_number());
        holder.sumggle_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CstomSumActivity.class);
                intent.putExtra("cstomName",sumggle.getSumggle_create());
                intent.putExtra("cstomNum",sumggle.getDelete_number());
                intent.putExtra("cstomSize",sumggleList.size()+"");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_left_in,R.anim.activity_left_out);
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

    //删除Notes
    public void removeNotes(int postion) {
        sumggleList.remove(postion);
        notifyItemRemoved(postion);
    }

    /*
    * 更新数据
    * */
    public void update(Sumggle sumggle, int postion) {

        notifyItemChanged(postion);
    }

    /*
    * 添加数据
    * */
    public void add(Sumggle sumggle) {
        sumggleList.add(0,sumggle);
        notifyItemInserted(0);
    }
}
