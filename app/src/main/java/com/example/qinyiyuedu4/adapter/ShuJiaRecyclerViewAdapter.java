package com.example.qinyiyuedu4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.pojo.Content;

import java.util.ArrayList;

public class ShuJiaRecyclerViewAdapter extends RecyclerView.Adapter<ShuJiaRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Content> mData_shu_ji;
    private Context mContext;
    private String duzhi;

    public ShuJiaRecyclerViewAdapter(Context context, ArrayList<Content> data_shuji) {
        mContext = context;
        mData_shu_ji = data_shuji;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_shujia_yangshi, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_shuji.setText(mData_shu_ji.get(position).getName());
        holder.zuozhe_shuji.setText("作者:" + mData_shu_ji.get(position).getZuo_zhe());
        holder.shuqian_shuji.setText("读至:" + mData_shu_ji.get(position).getDuzhi());
        holder.zuixin_shuji.setText(mData_shu_ji.get(position).getZui_xin());
        Glide.with(mContext).load(mData_shu_ji.get(position).getImg()).into(holder.image_shuji);

    }

    @Override
    public int getItemCount() {
        if (mData_shu_ji != null) {
            return mData_shu_ji.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name_shuji;
        private ImageView image_shuji;
        private TextView zuixin_shuji;
        private TextView zuozhe_shuji;
        private TextView shuqian_shuji;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_shuji = itemView.findViewById(R.id.sj_name);
            image_shuji = itemView.findViewById(R.id.sj_image);
            zuixin_shuji = itemView.findViewById(R.id.sj_zuixinzhangjie);
            zuozhe_shuji = itemView.findViewById(R.id.sj_zuozhe);
            shuqian_shuji = itemView.findViewById(R.id.sj_shuqian);

            //点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onRecyclerItemClick(getAdapterPosition());
                    }
                }
            });
            //长按事件
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //点击事件
    public OnRecyclerItemClickListener mOnItemClickListener;

    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(int position);
    }


    //长按事件
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.mOnItemLongClickListener = longClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
