package com.example.qinyiyuedu4.activity.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.ViewModel.SharedViewModel;
import com.example.qinyiyuedu4.activity.MainActivity;
import com.example.qinyiyuedu4.activity.YueDuActivity;
import com.example.qinyiyuedu4.adapter.ShuJiaRecyclerViewAdapter;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji_ji_lu;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;


public class BlankFragment_shujia extends Fragment {

    private View rootView;
    private ArrayList<Content> mData;
    private ShuJiaRecyclerViewAdapter adapter;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RefreshLayout refreshLayout;

    public BlankFragment_shujia() {
    }

    public BlankFragment_shujia(ArrayList<Content> data) {
        mData = data;
    }

    public static BlankFragment_shujia newInstance(String param1, String param2) {
        BlankFragment_shujia fragment = new BlankFragment_shujia();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_blank_shujia, container, false);
        }

        //获取fragment的控件
        initViews();
        return rootView;
    }



    private void initViews() {
        recyclerView = rootView.findViewById(R.id.fragment_recyclerview_shujia);
        refreshLayout=rootView.findViewById(R.id.refreshLayout_shijia);

        //上拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
              //对书架书籍信息进行更新
                refreshLayout.finishRefresh();
            }
        });


        //设置RecyclerView
        initRecyclerView();
    }




    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        //设置为倒序显示
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShuJiaRecyclerViewAdapter(getContext(), mData);
        recyclerView.setAdapter(adapter);
        //定位到最后一个
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
        //对recyclerview条目创建点击事件
        initListener();
    }

    //
    private void initListener() {
        adapter.setRecyclerItemClickListener(new ShuJiaRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                //点击进入阅读界面
                Intent intent=new Intent(getContext(),YueDuActivity.class);
                //把该书的地址传到阅读界面
                intent.putExtra("URL",mData.get(position).getHref());
                //把该书的书签传到阅读界面
                intent.putExtra("shuqian",mData.get(position).getShuqian());
                //把该书的读到章节名传到阅读界面
                intent.putExtra("name",mData.get(position).getName());
                startActivity(intent);

                upWeiZhi(position);

                //加入阅读记录
                YueDuJiLu(position);

            }
        });
        //长按事件
        adapter.setOnItemLongClickListener(new ShuJiaRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                //长按弹出弹窗
                showPopupWindow(position);
            }
        });
    }

    private void YueDuJiLu(int position) {
        //加入阅读记录
        DatabaseHelper_shu_ji_ji_lu databaseHelper_shu_ji_ji_lu=new DatabaseHelper_shu_ji_ji_lu(getContext());
        SQLiteDatabase db_shu_ji_ji_lu=databaseHelper_shu_ji_ji_lu.getWritableDatabase();
        //查询是否已加入书籍信息表
        Cursor cursor = db_shu_ji_ji_lu.query(Book.TABLE_NAME_SHU_JI_JI_LU, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
        if (!cursor.moveToNext()) {//没有记录的话，加入
            ContentValues values = new ContentValues();
            values.put("name", mData.get(position).getName());
            values.put("zuozhe", mData.get(position).getZuo_zhe());
            values.put("image", mData.get(position).getImg());
            values.put("href", mData.get(position).getHref());
            values.put("zuixin", mData.get(position).getZui_xin());
            values.put("jianjie", mData.get(position).getJianjie());
            values.put("shuqian", mData.get(position).getShuqian());
            values.put("duzhi", mData.get(position).getDuzhi());
            db_shu_ji_ji_lu.insert(Book.TABLE_NAME_SHU_JI_JI_LU,null,values);
        }
        db_shu_ji_ji_lu.close();
        databaseHelper_shu_ji_ji_lu.close();
    }

    private void upWeiZhi(int position) {
        DatabaseHelper_shu_ji databaseHelper_shu_ji=new DatabaseHelper_shu_ji(getContext());
        SQLiteDatabase db_shu_ji=databaseHelper_shu_ji.getWritableDatabase();
        //删除该书
        db_shu_ji.delete(Book.TABLE_NAME_SHU_JI, "rowid=" + (position + 1), null);
        String sql = "update " + Book.TABLE_NAME_SHU_JI + " set rowid=rowid-1 where rowid >" + (position + 1);
        db_shu_ji.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put("name", mData.get(position).getName());
        values.put("zuozhe", mData.get(position).getZuo_zhe());
        values.put("image", mData.get(position).getImg());
        values.put("href", mData.get(position).getHref());
        values.put("zuixin", mData.get(position).getZui_xin());
        values.put("jianjie", mData.get(position).getJianjie());
        values.put("shuqian", mData.get(position).getShuqian());
        values.put("duzhi", mData.get(position).getDuzhi());
        db_shu_ji.insert(Book.TABLE_NAME_SHU_JI,null,values);

        db_shu_ji.close();
        databaseHelper_shu_ji.close();
    }


    //弹窗
    private void showPopupWindow(int position) {
        //设置弹窗
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_main_popup_window, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //外部是否可点击 点击外部消失
        popupWindow.setOutsideTouchable(true);
        //响应返回键  点击返回键 消失
        popupWindow.setFocusable(true);

        //弹窗中得控件绑定,点击
        popupWindowBindingOnclick(position, contentView);


        //显示PopupWindow
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        //获取屏幕宽度
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        popupWindow.setWidth(width * 4 / 5);
        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

        //判断弹窗是否弹出，弹出则背景半透明
        if (popupWindow.isShowing()) {
            backgroundAlpha(0.5f);
        }
        //弹窗消失则恢复
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //弹窗的背景透明度
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    private void popupWindowBindingOnclick(int position, View contentView) {
        ImageView main_popupWindow_image = contentView.findViewById(R.id.main_popupWindow_image);
        Glide.with(getContext()).load(mData.get(position).getImg()).into(main_popupWindow_image);
        TextView main_popupWindow_textview_name = contentView.findViewById(R.id.main_popupWindow_textview_name);
        main_popupWindow_textview_name.setText(mData.get(position).getName());
        TextView main_popupWindow_textview_zuozhe = contentView.findViewById(R.id.main_popupWindow_textview_zuozhe);
        main_popupWindow_textview_zuozhe.setText(mData.get(position).getZuo_zhe());
        TextView main_popupWindow_textview_zuixin = contentView.findViewById(R.id.main_popupWindow_textview_zuixin);
        main_popupWindow_textview_zuixin.setText(mData.get(position).getZui_xin());
        TextView main_popupWindow_textview_jianjie = contentView.findViewById(R.id.main_popupWindow_textview_jianjie);
        main_popupWindow_textview_jianjie.setText(mData.get(position).getJianjie());
        //设置简介TextView可以滚动
        main_popupWindow_textview_jianjie.setMovementMethod(ScrollingMovementMethod.getInstance());

        //删除操作
        Button main_popupWindow_button_shanchu = contentView.findViewById(R.id.main_popupWindow_button_shanchu);
        main_popupWindow_button_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper_shu_ji databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
                SQLiteDatabase db_shu_ji = databaseHelper_shu_ji.getWritableDatabase();
                db_shu_ji.delete(Book.TABLE_NAME_SHU_JI, "rowid=" + (position + 1), null);
                String sql = "update " + Book.TABLE_NAME_SHU_JI + " set rowid=rowid-1 where rowid >" + (position + 1);
                db_shu_ji.execSQL(sql);
                db_shu_ji.close();
                databaseHelper_shu_ji.close();

                //更新籍数据库信息，放到集合中
                gengxinshujuku();
                //把新的书籍数据加入recyclerview
                initRecyclerView();

            }
        });
        //阅读操作
        Button main_popupWindow_button_yuedu = contentView.findViewById(R.id.main_popupWindow_button_yuedu);
        main_popupWindow_button_yuedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击进入阅读界面
                Intent intent=new Intent(getContext(),YueDuActivity.class);
                //把该书的地址传到阅读界面
                intent.putExtra("URL",mData.get(position).getHref());
                intent.putExtra("shuqian",mData.get(position).getShuqian());
                intent.putExtra("name",mData.get(position).getName());
                startActivity(intent);

                upWeiZhi(position);

                //加入阅读记录
                YueDuJiLu(position);

            }
        });

    }

    private void gengxinshujuku() {
        DatabaseHelper_shu_ji databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
        SQLiteDatabase db_shu_ji = databaseHelper_shu_ji.getWritableDatabase();
        mData = new ArrayList<>();
        //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer)
        Cursor cursor = db_shu_ji.query(Book.TABLE_NAME_SHU_JI, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name_shuji = cursor.getString(0);
            String zuozhe_shuji = cursor.getString(1);
            String image_shuji = cursor.getString(2);
            String href_shuji = cursor.getString(3);
            String zuixin_shuji = cursor.getString(4);
            String jianjie_shuji = cursor.getString(5);
            int shuqian_shuji = cursor.getInt(6);
            String duzhi_shuji = cursor.getString(7);

            Content content = new Content();
            content.setName(name_shuji);
            content.setZuo_zhe(zuozhe_shuji);
            content.setImg(image_shuji);
            content.setHref(href_shuji);
            content.setZui_xin(zuixin_shuji);
            content.setJianjie(jianjie_shuji);
            content.setShuqian(shuqian_shuji);
            content.setDuzhi(duzhi_shuji);

            mData.add(content);
        }
        //关闭
        cursor.close();
        db_shu_ji.close();
        databaseHelper_shu_ji.close();
    }

    //接收ViewModel信息进行书架更新
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), item -> {
            gengxinshujuku();
            //加载完成,让转圈停止
            Message message = new Message();
            message.what = 0111;
            handler.sendMessage(message);
        });
    }

    //处理信息
    public Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0111:
                    initRecyclerView();
                    break;
            }
        }
    };

}
