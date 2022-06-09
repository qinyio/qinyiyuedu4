package com.example.qinyiyuedu4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.pojo.Content_zj;

import java.util.ArrayList;


public class MuLuRecyclerViewAdapter extends RecyclerView.Adapter<MuLuRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Content_zj> mData_ZJ;
    private Context mContext;

    public MuLuRecyclerViewAdapter(Context context, ArrayList<Content_zj> data) {
        mContext = context;
        mData_ZJ = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_mulu_yangshi, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mMulu_zj_name.setText(mData_ZJ.get(position).getName_zj());

    }

    @Override
    public int getItemCount() {
        if (mData_ZJ != null) {
            return mData_ZJ.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mMulu_zj_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMulu_zj_name = itemView.findViewById(R.id.mulu_yangshi_textview_zj_name);

            //点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onRecyclerItemClick(getAdapterPosition());
                    }
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
}
