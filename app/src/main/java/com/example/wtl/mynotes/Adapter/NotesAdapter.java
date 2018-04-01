package com.example.wtl.mynotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.mynotes.Activity.EditNoteActivity;
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

    private OnItemLongClickListener onItemClickListener;
    private OnItemClickListener onItemClick;
    private Boolean longclick = false;
    private Boolean choose = true;

    private Animation long_img_come;
    private Animation long_img_gone;

    public NotesAdapter(List<Notes> list,Context context) {
        this.list = list;
        this.context = context;
        long_img_come = AnimationUtils.loadAnimation(context,R.anim.long_img_come);
        long_img_gone = AnimationUtils.loadAnimation(context,R.anim.long_img_gone);
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card,null,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.ViewHolder holder, final int position) {
        final Notes notes = list.get(position);
        holder.notes_content_part.setText(notes.getNotes_content_part());
        holder.notes_time.setText(notes.getNotes_time());
        //隐藏最后一个下划线
        /*if(position == list.size()-3) {
            holder.updownline.setVisibility(View.GONE);
        }*/
        if(longclick) {
            holder.check_box.setVisibility(View.VISIBLE);
            holder.check_box.startAnimation(long_img_come);
        } else {
            holder.check_box.setVisibility(View.GONE);
            holder.check_box.startAnimation(long_img_gone);
        }
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * 长按状态的逻辑
                * */
                if(longclick) {
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
                    onItemClick.OnItemClick(position,choose,list);
                } else if (!longclick) {  //正常状态的逻辑
                    Intent intent = new Intent(context, EditNoteActivity.class);
                    intent.putExtra("Postion",notes.getNotes_time());
                    intent.putExtra("State","change");
                    context.startActivity(intent);
                    ((Activity)context).finish();//因为context没有finish操作,将它强转为activity
                }
            }
        });
        holder.root_view.setBackground(context.getDrawable(R.color.white));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
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

    public void isLongItem(){
        longclick = false;
    }

    //删除Notes
    public void removeNotes(int postion) {
        list.remove(postion);
        notifyDataSetChanged();
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick();
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

}
