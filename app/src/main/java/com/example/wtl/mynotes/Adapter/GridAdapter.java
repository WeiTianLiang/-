package com.example.wtl.mynotes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.R;

import java.util.List;

/**
 * card布局适配器
 * Created by WTL on 2018/3/23.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<Notes> list;
    private Context context;

    private OnItemLongClickListener onItemClickListener;
    private OnItemClickListener onItemClick;
    private OnItemClickAloneListener aloneListener;
    private Boolean longclick = false;
    private Boolean choose = true;

    public GridAdapter(List<Notes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card_2,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GridAdapter.ViewHolder holder, final int position) {
        final Notes notes = list.get(position);
        holder.notes_content_part_2.setText(notes.getNotes_content_part());
        holder.notes_time_2.setText(notes.getNotes_time());

        if(longclick) {
            holder.check_card_box.setVisibility(View.VISIBLE);
        } else {
            holder.check_card_box.setVisibility(View.GONE);
        }
        holder.card_root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * 长按状态的逻辑
                * */
                if(longclick) {
                    if (holder.check_card_box.getDrawable().getCurrent().getConstantState().
                            equals(context.getResources().getDrawable(R.mipmap.circhosetouch).getConstantState())) {
                        holder.check_card_box.setImageResource(R.mipmap.circhose);
                        holder.card_root_view.setBackground(context.getDrawable(R.color.white));
                        choose = false;
                    } else {
                        holder.check_card_box.setImageResource(R.mipmap.circhosetouch);
                        holder.card_root_view.setBackground(context.getDrawable(R.color.longtouch));
                        choose = true;
                    }
                    onItemClick.OnItemClick(position,choose,list);
                } else if (!longclick) {  //正常状态的逻辑
                    aloneListener.OnItemAlone(notes,position,list);
                }
            }
        });
        holder.check_card_box.setImageResource(R.mipmap.circhose);
        holder.card_root_view.setBackground(context.getDrawable(R.color.white));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout card_root_view;
        TextView notes_content_part_2;
        TextView notes_time_2;
        ImageView check_card_box;

        public ViewHolder(View itemView) {
            super(itemView);
            card_root_view = itemView.findViewById(R.id.card_root_view);
            notes_content_part_2 = itemView.findViewById(R.id.notes_content_part_2);
            notes_time_2 = itemView.findViewById(R.id.notes_time_2);
            check_card_box = itemView.findViewById(R.id.check_card_box);

            card_root_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longclick = onItemClickListener.OnItemLongClick();
                    return true;
                }
            });
        }

    }

    public void isLongItem(){
        longclick = false;
        notifyDataSetChanged();
    }

    //删除Notes
    public void removeNotes(int postion) {
        list.remove(postion);
        notifyItemRemoved(postion);
    }

    /*
    * 添加数据
    * */
    public void add(Notes notes) {
        list.add(0,notes);
        notifyItemInserted(0);
        notifyItemRangeChanged(0,list.size());
    }
    /*
    * 更新数据
    * */
    public void update(Notes notes,int postion) {
        list.get(postion).setNotes_content_part(notes.getNotes_content_part());
        list.get(postion).setNotes_time(notes.getNotes_time());
        notifyItemChanged(postion);
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick();
    }

    public interface OnItemClickAloneListener {
        void OnItemAlone(Notes notes,int pos,List<Notes> list);
    }

    public interface OnItemClickListener {
        void OnItemClick(int x,boolean adro,List<Notes> list);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemClickAloneListener(OnItemClickAloneListener onItemClickAloneListener) {
        this.aloneListener = onItemClickAloneListener;
    }

}
