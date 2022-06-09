package com.example.qinyiyuedu4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.adapter.SouSuoRecyclerViewAdapter;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji_ji_lu;

import java.util.ArrayList;

public class YeuDuJiLuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseHelper_shu_ji_ji_lu databaseHelperShuJiJilu;
    private SQLiteDatabase db_shu_ji_ji_lu;
    private ArrayList<Content> mData_jilu;
    private SouSuoRecyclerViewAdapter mAdapter;
    private PopupWindow popupWindow;
    private ContentValues values1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_du_ji_lu);
        mRecyclerView = findViewById(R.id.jilu_recyclerview);
        //获取记录数据
        initjilu();
        //
        initRecyclerview();
    }

    private void initRecyclerview() {
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(YeuDuJiLuActivity.this);
        //设置为倒序
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        //创建适配器
        mAdapter = new SouSuoRecyclerViewAdapter(YeuDuJiLuActivity.this, mData_jilu);
        mRecyclerView.setAdapter(mAdapter);
        //定位到最后一个
        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        //对recyclerview条目创建点击事件
        initListener();
    }

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
        View contentView = LayoutInflater.from(YeuDuJiLuActivity.this).inflate(R.layout.layout_sousuo_popup_window, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //外部是否可点击 点击外部消失
        popupWindow.setOutsideTouchable(true);
        //响应返回键  点击返回键 消失
        popupWindow.setFocusable(true);

        //弹窗中得控件绑定,点击
        popupWindowBindingOnclick(position, contentView);

        //显示PopupWindow
        View rootview = LayoutInflater.from(YeuDuJiLuActivity.this).inflate(R.layout.activity_yeu_du_ji_lu, null);
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

    private void upWeiZhi(int position) {
        DatabaseHelper_shu_ji_ji_lu databaseHelper_shu_ji_ji_lu = new DatabaseHelper_shu_ji_ji_lu(getApplication());
        SQLiteDatabase db_shu_ji_ji_lu = databaseHelper_shu_ji_ji_lu.getWritableDatabase();
        //删除该书
        db_shu_ji_ji_lu.delete(Book.TABLE_NAME_SHU_JI_JI_LU, "rowid=" + (position + 1), null);
        String sql = "update " + Book.TABLE_NAME_SHU_JI_JI_LU + " set rowid=rowid-1 where rowid >" + (position + 1);
        db_shu_ji_ji_lu.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put("name", mData_jilu.get(position).getName());
        values.put("zuozhe", mData_jilu.get(position).getZuo_zhe());
        values.put("image", mData_jilu.get(position).getImg());
        values.put("href", mData_jilu.get(position).getHref());
        values.put("zuixin", mData_jilu.get(position).getZui_xin());
        values.put("jianjie", mData_jilu.get(position).getJianjie());
        values.put("shuqian", mData_jilu.get(position).getShuqian());
        values.put("duzhi", mData_jilu.get(position).getDuzhi());
        db_shu_ji_ji_lu.insert(Book.TABLE_NAME_SHU_JI_JI_LU, null, values);

        db_shu_ji_ji_lu.close();
        databaseHelper_shu_ji_ji_lu.close();
    }

    private void popupWindowBindingOnclick(int position, View contentView) {
        ImageView popupWindow_image = contentView.findViewById(R.id.popupWindow_image);
        Glide.with(YeuDuJiLuActivity.this).load(mData_jilu.get(position).getImg()).into(popupWindow_image);
        TextView popupWindow_textview_name = contentView.findViewById(R.id.popupWindow_textview_name);
        popupWindow_textview_name.setText(mData_jilu.get(position).getName());
        TextView popupWindow_textview_zuozhe = contentView.findViewById(R.id.popupWindow_textview_zuozhe);
        popupWindow_textview_zuozhe.setText(mData_jilu.get(position).getZuo_zhe());
        TextView popupWindow_textview_zuixin = contentView.findViewById(R.id.popupWindow_textview_zuixin);
        popupWindow_textview_zuixin.setText(mData_jilu.get(position).getZui_xin());
        TextView popupWindow_textview_jianjie = contentView.findViewById(R.id.popupWindow_textview_jianjie);
        popupWindow_textview_jianjie.setText(mData_jilu.get(position).getJianjie());
        //设置简介TextView可以滚动
        popupWindow_textview_jianjie.setMovementMethod(ScrollingMovementMethod.getInstance());

        //处理点击阅读事件
        Button popupWindow_button_yuedu = contentView.findViewById(R.id.popupWindow_button_yuedu);
        popupWindow_button_yuedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到阅读界面
                Intent intent = new Intent(YeuDuJiLuActivity.this, YueDuActivity.class);
                //把该书的地址传到阅读界面
                intent.putExtra("URL", mData_jilu.get(position).getHref());
                intent.putExtra("shuqian", mData_jilu.get(position).getShuqian());
                intent.putExtra("name", mData_jilu.get(position).getName());
                startActivity(intent);

                upWeiZhi(position);
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
                values.put("name", mData_jilu.get(position).getName());
                values.put("zuozhe", mData_jilu.get(position).getZuo_zhe());
                values.put("image", mData_jilu.get(position).getImg());
                values.put("href", mData_jilu.get(position).getHref());
                values.put("zuixin", mData_jilu.get(position).getZui_xin());
                values.put("jianjie", mData_jilu.get(position).getJianjie());
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

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    private void initjilu() {
        mData_jilu = new ArrayList<>();
        databaseHelperShuJiJilu = new DatabaseHelper_shu_ji_ji_lu(getApplication());
        db_shu_ji_ji_lu = databaseHelperShuJiJilu.getWritableDatabase();

        Cursor cursor = db_shu_ji_ji_lu.query(Book.TABLE_NAME_SHU_JI_JI_LU, null, null, null, null, null, null);
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

            mData_jilu.add(content);
        }
        cursor.close();
        db_shu_ji_ji_lu.close();
        databaseHelperShuJiJilu.close();
    }
}