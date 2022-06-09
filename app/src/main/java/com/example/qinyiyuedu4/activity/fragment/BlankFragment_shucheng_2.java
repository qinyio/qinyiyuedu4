package com.example.qinyiyuedu4.activity.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.activity.MainActivity;
import com.example.qinyiyuedu4.activity.YueDuActivity;
import com.example.qinyiyuedu4.adapter.ShuCheng2RecyclerViewAdapter;
import com.example.qinyiyuedu4.html.HtmlShuCheng;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji_ji_lu;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class BlankFragment_shucheng_2 extends Fragment {
    private View rootView;
    private String keyword;
    private RecyclerView mRecyclerView;
    private ShuCheng2RecyclerViewAdapter mAdapter;
    private PopupWindow popupWindow;

    private DatabaseHelper_shu_ji_ji_lu databaseHelperShuJiJilu;
    private SQLiteDatabase db_shu_ji_ji_lu;
    private ArrayList<Content> mData;
    private ContentValues values;
    private RefreshLayout refreshLayout;
    private int N = 1;
    private ArrayList<Content> mData2;
    private DatabaseHelper_shu_ji databaseHelper_shu_ji;
    private SQLiteDatabase db_shuji;
    private boolean TF;


    public BlankFragment_shucheng_2() {
    }

    public BlankFragment_shucheng_2(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_blank_shucheng_2, container, false);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //信息传递
                Message message = new Message();
                message.what = 1000;
                handler.sendMessage(message);
                //获取数据
                initData(keyword);
            }
        }).start();


        //找到控件
        initViews();
        return rootView;
    }

    private void initData(String keyword) {
        HtmlShuCheng htmlShuCheng = new HtmlShuCheng();
        mData = htmlShuCheng.parseShuCheng(keyword);

        //加载完成,让转圈停止
        Message message = new Message();
        message.what = 1001;
        handler.sendMessage(message);
    }


    private void initViews() {
        mRecyclerView = rootView.findViewById(R.id.shucheng2_recyclerview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        //上拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initData(keyword);
                    }
                }).start();
            }
        });
        //下拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                N++;
                String keyword2 = "chn21/year2022-month05-page" + N;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initData2(keyword2);
                    }
                }).start();
            }
        });
    }

    private void initData2(String keyword2) {
        HtmlShuCheng htmlShuCheng = new HtmlShuCheng();
        mData2 = htmlShuCheng.parseShuCheng(keyword2);
        if (mData2.size() != 0) {
            //加载完成,让转圈停止
            Message message = new Message();
            message.what = 1010;
            handler.sendMessage(message);
        } else {
            //获取数据为空即为末页
            Message message = new Message();
            message.what = 0101;
            handler.sendMessage(message);
        }
    }

    private void initListener() {
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //创建适配器
        mAdapter = new ShuCheng2RecyclerViewAdapter(getContext(), mData);
        System.out.println("======================");
        System.out.println(mData);
        mRecyclerView.setAdapter(mAdapter);

        //点击事件
        onClickRecyclerView();
    }

    private void onClickRecyclerView() {
        mAdapter.setRecyclerItemClickListener(new ShuCheng2RecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                showPopupWindow(position);
                databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
                db_shuji = databaseHelper_shu_ji.getWritableDatabase();
                //查询是否已加入书架
                Cursor cursor = db_shuji.query(Book.TABLE_NAME_SHU_JI, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                if (cursor.moveToNext()) {
                    //已存在该书籍
                    TF = true;
                } else {
                    TF = false;
                }
                cursor.close();
                db_shuji.close();
                databaseHelper_shu_ji.close();
            }
        });
    }

    private void showPopupWindow(int position) {
        //设置弹窗
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sousuo_popup_window, null);
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

    private MainActivity.Fragment1CallBack fragment1CallBack;
    private void popupWindowBindingOnclick(int position, View contentView) {

        fragment1CallBack.buttonClick1();


        ImageView popupWindow_image = contentView.findViewById(R.id.popupWindow_image);
        Glide.with(getContext()).load(mData.get(position).getImg()).into(popupWindow_image);
        TextView popupWindow_textview_name = contentView.findViewById(R.id.popupWindow_textview_name);
        popupWindow_textview_name.setText(mData.get(position).getName());
        TextView popupWindow_textview_zuozhe = contentView.findViewById(R.id.popupWindow_textview_zuozhe);
        popupWindow_textview_zuozhe.setText(mData.get(position).getZuo_zhe());
        TextView popupWindow_textview_zuixin = contentView.findViewById(R.id.popupWindow_textview_zuixin);
        popupWindow_textview_zuixin.setText(mData.get(position).getZui_xin());
        TextView popupWindow_textview_jianjie = contentView.findViewById(R.id.popupWindow_textview_jianjie);
        popupWindow_textview_jianjie.setText(mData.get(position).getJianjie());
        //设置简介TextView可以滚动
        popupWindow_textview_jianjie.setMovementMethod(ScrollingMovementMethod.getInstance());
        //处理点击阅读事件
        Button popupWindow_button_yuedu = contentView.findViewById(R.id.popupWindow_button_yuedu);
        //处理点击加入书架事件
        Button popupWindow_button_jiaru = contentView.findViewById(R.id.popupWindow_button_jiaru);

        popupWindow_button_yuedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到阅读界面
                Intent intent = new Intent(getContext(), YueDuActivity.class);
                //把该书的地址传到阅读界面
                intent.putExtra("URL", mData.get(position).getHref());
                intent.putExtra("shuqian", mData.get(position).getShuqian());
                intent.putExtra("name", mData.get(position).getName());
                startActivity(intent);
                popupWindow.dismiss();

                //添加阅读记录
                databaseHelperShuJiJilu = new DatabaseHelper_shu_ji_ji_lu(getContext());
                db_shu_ji_ji_lu = databaseHelperShuJiJilu.getWritableDatabase();

                // Cursor cursor = db_shu_ji_ji_lu.query(Book.TABLE_NAME_SHU_JI_JI_LU, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)
                if (!TF) {
                    values = new ContentValues();
                    values.put("name", mData.get(position).getName());
                    values.put("zuozhe", mData.get(position).getZuo_zhe());
                    values.put("image", mData.get(position).getImg());
                    values.put("href", mData.get(position).getHref());
                    values.put("zuixin", mData.get(position).getZui_xin());
                    values.put("jianjie", mData.get(position).getJianjie());
                    values.put("shuqian", mData.get(position).getShuqian());
                    values.put("duzhi", mData.get(position).getDuzhi());
                    db_shu_ji_ji_lu.insert(Book.TABLE_NAME_SHU_JI_JI_LU, null, values);
                }
                db_shu_ji_ji_lu.close();
                databaseHelperShuJiJilu.close();
            }
        });
        //点击加入
        popupWindow_button_jiaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建数据库放书籍
                databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
                //表中创建字段：书名，作者，图片地址，书籍地址，简介，书签
                //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)
                db_shuji = databaseHelper_shu_ji.getWritableDatabase();

                //查询是否已加入书籍信息表
                Cursor cursor = db_shuji.query(Book.TABLE_NAME_SHU_JI, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                if (!cursor.moveToNext()) {
                    //加入表中
                    ContentValues values = new ContentValues();
                    values.put("name", mData.get(position).getName());
                    values.put("zuozhe", mData.get(position).getZuo_zhe());
                    values.put("image", mData.get(position).getImg());
                    values.put("href", mData.get(position).getHref());
                    values.put("zuixin", mData.get(position).getZui_xin());
                    values.put("jianjie", mData.get(position).getJianjie());
                    //第一次添加书签定到第一章
                    values.put("shuqian", 0);
                    values.put("duzhi", 0);
                    db_shuji.insert(Book.TABLE_NAME_SHU_JI, null, values);
                }
                //关闭
                db_shuji.close();
                databaseHelper_shu_ji.close();
                popupWindow_button_jiaru.setText("已加入书架");
                ;

            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    private void showPopup() {
        //设置弹窗
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.loading_popup, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //外部是否可点击 点击外部消失
        popupWindow.setOutsideTouchable(false);
        //响应返回键  点击返回键 消失
        popupWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_sou_suo, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

    }


    //处理信息
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1000:
                        showPopup();
                        break;
                    case 1001:
                        popupWindow.dismiss();
                        //对recyclerview操作
                        initListener();
                        //更新列表
                        mAdapter.notifyDataSetChanged();
                        //下拉刷新停止
                        refreshLayout.finishRefresh();
                        break;
                    case 1010:
                        mData.addAll(mData2);
                        //更新列表
                        mAdapter.notifyDataSetChanged();
                        //上拉加载更多停止
                        refreshLayout.finishLoadMore();
                        break;
                    case 0101:
                        Toast.makeText(getContext(), "人家也是有底线的...", Toast.LENGTH_LONG).show();
                        //上拉加载更多停止
                        refreshLayout.finishLoadMore();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}