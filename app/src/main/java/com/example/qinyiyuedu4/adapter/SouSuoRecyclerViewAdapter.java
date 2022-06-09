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

import java.util.List;

public class SouSuoRecyclerViewAdapter extends RecyclerView.Adapter<SouSuoRecyclerViewAdapter.ViewHolder> {

    private List<Content> mData;
    private Context mContext;

    public SouSuoRecyclerViewAdapter(Context context, List<Content> data) {
        mData = data;
        mContext = context;
    }

    //设置样式
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_sousuo_yangshi, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //绑定数据
        Content content = mData.get(position);
        holder.mName.setText(content.getName());
        holder.mZuixinzhangjie.setText(content.getZui_xin());
        holder.mZuozhe.setText(content.getZuo_zhe());
        Glide.with(mContext).load(content.getImg()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mZuozhe;
        private TextView mZuixinzhangjie;
        private ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.shuji_name);
            mZuozhe = itemView.findViewById(R.id.shuji_zuozhe);
            mZuixinzhangjie = itemView.findViewById(R.id.shuji_zuixinzhangjie);
            mImage = itemView.findViewById(R.id.shuji_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onRecyclerItemClick(getAdapterPosition());
                    }
                }
            });

//            mName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onRecyclerItemClick(getAdapterPosition());
//                    }
//                }
//            });

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
}
