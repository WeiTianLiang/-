package com.example.wtl.mynotes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Class.Notes;
import com.example.wtl.mynotes.R;

import java.util.List;


/**
 * list布局适配器
 * Created by WTL on 2018/3/22.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Notes> list;
    private Context context;

    private OnItemLongClickListener onItemClickListener;
    private OnItemClickListener onItemClick;
    private OnItemClickAloneListener aloneListener;
    private Boolean longclick = false;//是否为长按
    private Boolean choose = true;//是否选中

    public NotesAdapter(List<Notes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.ViewHolder holder, final int position) {
        final Notes notes = list.get(position);
        holder.notes_content_part.setText(notes.getNotes_content_part());
        holder.notes_time.setText(notes.getNotes_time());

        if (longclick) {
            holder.check_box.setVisibility(View.VISIBLE);//设置长按后list边的圆为选中状态
        } else {
            holder.check_box.setVisibility(View.GONE);//设置长按后list边的圆为未选中状态
        }
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * 长按状态的逻辑
                * */
                if (longclick) {
                    if (holder.check_box.getDrawable().getCurrent().getConstantState().
                            equals(context.getResources().getDrawable(R.mipmap.circhosetouch).getConstantState())) {
                        holder.check_box.setImageResource(R.mipmap.circhose);
                        holder.root_view.setBackground(context.getDrawable(R.color.white));
                        choose = false;
                    } else {
                        holder.check_box.setImageResource(R.mipmap.circhosetouch);
                        holder.root_view.setBackground(context.getDrawable(R.color.longtouch));
                        choose = true;
                    }
                    onItemClick.OnItemClick(position, choose, list);
                } else if (!longclick) {  //正常状态的逻辑
                    aloneListener.OnItemAlone(notes, position, list);//点击跳转另一个界面
                }
            }
        });
        holder.check_box.setImageResource(R.mipmap.circhose);
        holder.root_view.setBackground(context.getDrawable(R.color.white));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root_view;
        TextView notes_content_part;
        TextView notes_time;
        ImageView check_box;

        public ViewHolder(View itemView) {
            super(itemView);
            notes_content_part = itemView.findViewById(R.id.notes_content_part);
            notes_time = itemView.findViewById(R.id.notes_time);
            check_box = itemView.findViewById(R.id.check_box);
            root_view = itemView.findViewById(R.id.root_view);

            root_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longclick = onItemClickListener.OnItemLongClick();
                    return true;
                }
            });

        }

    }

    /*
    * 判断当前是否为长按状态
    * */
    public void isLongItem() {
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
    }

    /*
    * 更新数据
    * */
    public void update(Notes notes,int postion) {
        list.get(postion).setNotes_content_part(notes.getNotes_content_part());
        list.get(postion).setNotes_time(notes.getNotes_time());
        notifyItemChanged(postion);
    }

    /*
    * 长按跳转接口
    * */
    public interface OnItemLongClickListener {
        boolean OnItemLongClick();
    }

    /*
    * 长按后单选接口
    * */
    public interface OnItemClickListener {
        void OnItemClick(int x, boolean adro, List<Notes> list);
    }

    /*
    * 单选跳转接口
    * */
    public interface OnItemClickAloneListener {
        void OnItemAlone(Notes notes, int pos, List<Notes> list);
    }

    /*
    * 长按后单选外部接口
    * */
    public void setOnItemClickListener(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    /*
    * 长按外部跳转接口
    * */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /*
    * 单选跳转外部接口
    * */
    public void setOnItemClickAloneListener(OnItemClickAloneListener onItemClickAloneListener) {
        this.aloneListener = onItemClickAloneListener;
    }

}
