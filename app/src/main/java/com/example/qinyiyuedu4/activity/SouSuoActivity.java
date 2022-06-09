package com.example.qinyiyuedu4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.adapter.SouSuoRecyclerViewAdapter;
import com.example.qinyiyuedu4.html.Html_ShuJIXinXI;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji_ji_lu;

import java.util.List;

public class SouSuoActivity extends AppCompatActivity {

    private EditText mEditText_sousuo;
    private Button mButton_sousuo;
    private String keywords;
    private RecyclerView mRecyclerview;
    private SouSuoRecyclerViewAdapter mAdapter;
    //集合放搜索到的书籍的基本信息
    private List<Content> mData;
    private PopupWindow popupWindow;
    private DatabaseHelper_shu_ji_ji_lu databaseHelperShuJiJilu;
    private SQLiteDatabase db_shu_ji_ji_lu;
    private ContentValues values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_suo);

        //找到控件,点击事件
        initViews();

    }

    private void initViews() {
        //找到控件
        mEditText_sousuo = findViewById(R.id.sousuo_edittext);
        mButton_sousuo = findViewById(R.id.sousuo_button);
        mRecyclerview = findViewById(R.id.sousuo_recyclerview);

        //点击事件
        mButton_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取搜索框的内容
                keywords = mEditText_sousuo.getText().toString();
                //获取数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initData(keywords);
                        //信息传递
                        Message message = new Message();
                        message.what = 0000;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    //处理信息
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0000:
                        //创建布局管理器
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SouSuoActivity.this);
                        mRecyclerview.setLayoutManager(layoutManager);
                        //创建适配器
                        mAdapter = new SouSuoRecyclerViewAdapter(SouSuoActivity.this, mData);
                        mRecyclerview.setAdapter(mAdapter);

                        //对recyclerview条目创建点击事件
                        initListener();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //recyclerview的点击事件
    private void initListener() {
        mAdapter.setRecyclerItemClickListener(new SouSuoRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                showPopupWindow(position);

            }
        });
    }





    private void showPopupWindow(int position) {
        //设置弹窗
        View contentView = LayoutInflater.from(SouSuoActivity.this).inflate(R.layout.layout_sousuo_popup_window, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //外部是否可点击 点击外部消失
        popupWindow.setOutsideTouchable(true);
        //响应返回键  点击返回键 消失
        popupWindow.setFocusable(true);

        //弹窗中得控件绑定,点击
        popupWindowBindingOnclick(position, contentView);

        //显示PopupWindow
        View rootview = LayoutInflater.from(SouSuoActivity.this).inflate(R.layout.activity_sou_suo, null);
        //获取屏幕宽度
        int width = getWindowManager().getDefaultDisplay().getWidth();
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

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    private void popupWindowBindingOnclick(int position, View contentView) {
        ImageView popupWindow_image = contentView.findViewById(R.id.popupWindow_image);
        Glide.with(SouSuoActivity.this).load(mData.get(position).getImg()).into(popupWindow_image);
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
        popupWindow_button_yuedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到阅读界面
                Intent intent = new Intent(SouSuoActivity.this, YueDuActivity.class);
                //把该书的地址传到阅读界面
                intent.putExtra("URL",mData.get(position).getHref());
                intent.putExtra("shuqian",mData.get(position).getShuqian());
                intent.putExtra("name",mData.get(position).getName());
                startActivity(intent);

                //添加阅读记录
                databaseHelperShuJiJilu = new DatabaseHelper_shu_ji_ji_lu(getApplication());
                db_shu_ji_ji_lu = databaseHelperShuJiJilu.getWritableDatabase();

                Cursor cursor = db_shu_ji_ji_lu.query(Book.TABLE_NAME_SHU_JI_JI_LU, null, "name=?", new String[]{mData.get(position).getName()}, null, null, null);
                //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)
                if (!cursor.moveToNext()) {
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

        //处理点击加入书架事件
        Button popupWindow_button_jiaru = contentView.findViewById(R.id.popupWindow_button_jiaru);
        popupWindow_button_jiaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //创建数据库放书籍
                DatabaseHelper_shu_ji databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getApplication());
                //表中创建字段：书名，作者，图片地址，书籍地址，简介，书签
                //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)
                SQLiteDatabase db_shuji = databaseHelper_shu_ji.getWritableDatabase();

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
                //关闭
                db_shuji.close();
                databaseHelper_shu_ji.close();
                popupWindow_button_jiaru.setText("已加入书架");
            }
        });

    }


    //获取数据，并加入recyclerview
    private void initData(String keywords) {
        Html_ShuJIXinXI htmlUtilShujixinxi = new Html_ShuJIXinXI();
        mData = htmlUtilShujixinxi.parseSouSuo(keywords);
    }
}