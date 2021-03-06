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
import androidx.lifecycle.ViewModelProvider;
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
import com.example.qinyiyuedu4.ViewModel.SharedViewModel;
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

    private SharedViewModel model;



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
                //????????????
                Message message = new Message();
                message.what = 1000;
                handler.sendMessage(message);
                //????????????
                initData(keyword);
            }
        }).start();

        //??????ViewModel??????????????????
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //????????????
        initViews();
        return rootView;
    }

    private void initData(String keyword) {
        HtmlShuCheng htmlShuCheng = new HtmlShuCheng();
        mData = htmlShuCheng.parseShuCheng(keyword);

        //????????????,???????????????
        Message message = new Message();
        message.what = 1001;
        handler.sendMessage(message);
    }


    private void initViews() {
        mRecyclerView = rootView.findViewById(R.id.shucheng2_recyclerview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        //????????????
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
        //??????????????????
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
            //????????????,???????????????
            Message message = new Message();
            message.what = 1010;
            handler.sendMessage(message);
        } else {
            //??????????????????????????????
            Message message = new Message();
            message.what = 0101;
            handler.sendMessage(message);
        }
    }

    private void initListener() {
        //?????????????????????
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //???????????????
        mAdapter = new ShuCheng2RecyclerViewAdapter(getContext(), mData);
        System.out.println("======================");
        System.out.println(mData);
        mRecyclerView.setAdapter(mAdapter);

        //????????????
        onClickRecyclerView();
    }

    private void onClickRecyclerView() {
        mAdapter.setRecyclerItemClickListener(new ShuCheng2RecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                showPopupWindow(position);
                databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
                db_shuji = databaseHelper_shu_ji.getWritableDatabase();
                //???????????????????????????
                Cursor cursor = db_shuji.query(Book.TABLE_NAME_SHU_JI, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                if (cursor.moveToNext()) {
                    //??????????????????
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
        //????????????
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sousuo_popup_window, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //????????????????????? ??????????????????
        popupWindow.setOutsideTouchable(true);
        //???????????????  ??????????????? ??????
        popupWindow.setFocusable(true);


        //????????????????????????,??????
        popupWindowBindingOnclick(position, contentView);

        //??????PopupWindow
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        //??????????????????
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        popupWindow.setWidth(width * 4 / 5);
        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

        //???????????????????????????????????????????????????
        if (popupWindow.isShowing()) {
            backgroundAlpha(0.5f);
        }
        //?????????????????????
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


    }

    private void popupWindowBindingOnclick(int position, View contentView) {

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
        //????????????TextView????????????
        popupWindow_textview_jianjie.setMovementMethod(ScrollingMovementMethod.getInstance());
        //????????????????????????
        Button popupWindow_button_yuedu = contentView.findViewById(R.id.popupWindow_button_yuedu);
        //??????????????????????????????
        Button popupWindow_button_jiaru = contentView.findViewById(R.id.popupWindow_button_jiaru);

        popupWindow_button_yuedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????????????????????
                Intent intent = new Intent(getContext(), YueDuActivity.class);
                //????????????????????????????????????
                intent.putExtra("URL", mData.get(position).getHref());
                intent.putExtra("shuqian", mData.get(position).getShuqian());
                intent.putExtra("name", mData.get(position).getName());
                startActivity(intent);
                popupWindow.dismiss();

                //??????????????????
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


        //????????????
        popupWindow_button_jiaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????????
                databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getContext());
                //????????????????????????????????????????????????????????????????????????????????????
                //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)
                db_shuji = databaseHelper_shu_ji.getWritableDatabase();

                //????????????????????????????????????
                Cursor cursor = db_shuji.query(Book.TABLE_NAME_SHU_JI, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                if (!cursor.moveToNext()) {
                    //????????????
                    ContentValues values = new ContentValues();
                    values.put("name", mData.get(position).getName());
                    values.put("zuozhe", mData.get(position).getZuo_zhe());
                    values.put("image", mData.get(position).getImg());
                    values.put("href", mData.get(position).getHref());
                    values.put("zuixin", mData.get(position).getZui_xin());
                    values.put("jianjie", mData.get(position).getJianjie());
                    //????????????????????????????????????
                    values.put("shuqian", 0);
                    values.put("duzhi", 0);
                    db_shuji.insert(Book.TABLE_NAME_SHU_JI, null, values);
                    //??????ViewModel??????????????????
                    model.select("gengxin");
                }
                //??????
                db_shuji.close();
                databaseHelper_shu_ji.close();
                popupWindow_button_jiaru.setText("???????????????");

            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    private void showPopup() {
        //????????????
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.loading_popup, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //????????????????????? ??????????????????
        popupWindow.setOutsideTouchable(false);
        //???????????????  ??????????????? ??????
        popupWindow.setFocusable(true);
        //??????PopupWindow
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_sou_suo, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

    }


    //????????????
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
                        //???recyclerview??????
                        initListener();
                        //????????????
                        mAdapter.notifyDataSetChanged();
                        //??????????????????
                        refreshLayout.finishRefresh();
                        break;
                    case 1010:
                        mData.addAll(mData2);
                        //????????????
                        mAdapter.notifyDataSetChanged();
                        //????????????????????????
                        refreshLayout.finishLoadMore();
                        break;
                    case 0101:
                        Toast.makeText(getContext(), "????????????????????????...", Toast.LENGTH_LONG).show();
                        //????????????????????????
                        refreshLayout.finishLoadMore();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}