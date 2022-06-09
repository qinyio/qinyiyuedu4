package com.example.qinyiyuedu4.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.activity.fragment.BlankFragment_shucheng;
import com.example.qinyiyuedu4.activity.fragment.BlankFragment_shujia;
import com.example.qinyiyuedu4.adapter.FragmentAdapter;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_ziti;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextview_caidan, mTextview_caidan2;
    private EditText mEditText;
    private TextView mTextview_shujia, mTextview_shucheng, mTextview;
    private DrawerLayout mDrawerLayout;
    private ViewPager2 mViewpager;
    private ArrayList<Content> mData_shu_ji;
    private FragmentAdapter fragmentAdapter;
    private TextView mTextView_cehua_mianze;
    private TextView mTextView_cehua_yudujilu;
    private TextView mTextView_fenxiang;
    private TextView mTextView_dashang;
    private LinearLayout mLinearLayout_cehualan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建字体数据库初始化设置
        zitishujukushezhi();
        //获取书籍数据库信息，放到集合中
        shujixinxishujuku();
        //找到控件
        initViews();

        //侧滑数据
        initCehuaRecyclerview();


    }

    private void initCehuaRecyclerview() {
        //获取当前屏幕大小
        WindowManager windowManager = this.getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = mLinearLayout_cehualan.getLayoutParams();
        params.width = width * 4 / 5;
    }


    //刷新activity
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        //过场动画
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void shujixinxishujuku() {
        //表中创建字段：书名，作者,图片地址，书籍地址,最新，简介，书签,读至,读到的一章内容
        DatabaseHelper_shu_ji databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getApplication());
        SQLiteDatabase db_shu_ji = databaseHelper_shu_ji.getWritableDatabase();
        mData_shu_ji = new ArrayList<>();
        //(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar,txt varchar)
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

            mData_shu_ji.add(content);
        }
        //关闭
        cursor.close();
        db_shu_ji.close();
        databaseHelper_shu_ji.close();
    }

    private void zitishujukushezhi() {
        DatabaseHelper_ziti databaseHelper_ziti = new DatabaseHelper_ziti(MainActivity.this);
        SQLiteDatabase db = databaseHelper_ziti.getWritableDatabase();
        db.close();
        databaseHelper_ziti.close();
    }

    private void initViews() {
        mEditText = findViewById(R.id.main_edittext_sousuo);
        mDrawerLayout = findViewById(R.id.main_drawerlayout);
        mViewpager = findViewById(R.id.main_viewpager);
        mTextview_caidan = findViewById(R.id.main_textview_caidan);
        mTextview_caidan2 = findViewById(R.id.main_textview_caidan2);
        mTextview_shujia = findViewById(R.id.main_textview_shujia);
        mTextview_shucheng = findViewById(R.id.main_textview_shucheng);
        mTextView_cehua_mianze = findViewById(R.id.cehua_textview_mianzeshengming);
        mTextView_cehua_yudujilu = findViewById(R.id.cehua_textview_yuedujilu);
        mTextView_fenxiang = findViewById(R.id.cehua_textview_fenxiang);
        mTextView_dashang = findViewById(R.id.cehua_textview_dashang);
        mLinearLayout_cehualan = findViewById(R.id.main_linearlayout_cehualan);

        mTextView_cehua_mianze.setOnClickListener(this);
        mTextView_cehua_yudujilu.setOnClickListener(this);
        mTextView_dashang.setOnClickListener(this);
        mTextview_caidan.setOnClickListener(this);
        mTextview_caidan2.setOnClickListener(this);
        mTextview_shujia.setOnClickListener(this);
        mTextview_shucheng.setOnClickListener(this);
        mTextView_fenxiang.setOnClickListener(this);
        mEditText.setOnClickListener(this);

        //默认焦点位置
        mTextview_shujia.setSelected(true);
        mTextview = mTextview_shujia;

        //fragment填充viewpager
        initPager();
    }

    private void initPager() {

        //Fragment加入集合，集合填充进viewpapger
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new BlankFragment_shujia(mData_shu_ji));
        fragments.add(new BlankFragment_shucheng());

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        mViewpager.setAdapter(fragmentAdapter);

        //viewPager滑动监听
        mViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //滑到对应fragment页面
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void changeTab(int position) {
        mTextview.setSelected(false);
        switch (position) {
            case R.id.main_textview_shujia:
                mViewpager.setCurrentItem(0);
            case 0:
                mTextview_shujia.setSelected(true);
                mTextview = mTextview_shujia;
                break;
            case R.id.main_textview_shucheng:
                mViewpager.setCurrentItem(1);
            case 1:
                mTextview_shucheng.setSelected(true);
                mTextview = mTextview_shucheng;
                break;
        }
    }

    public void allShare() {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "share");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "https://www.aliyundrive.com/s/uCGPXPhkJt1");//添加分享内容
        //创建分享的对话框
        share_intent = Intent.createChooser(share_intent, "");
        startActivity(share_intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_textview_caidan:
                //打开滑动菜单  左侧出现
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_textview_shujia:
            case R.id.main_textview_shucheng:
                changeTab(v.getId());
                break;
            case R.id.main_edittext_sousuo:
                Intent intent = new Intent(MainActivity.this, SouSuoActivity.class);
                startActivity(intent);
                break;
            case R.id.cehua_textview_mianzeshengming:
                Intent intent1 = new Intent(MainActivity.this, ShengMingActivity.class);
                startActivity(intent1);
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.cehua_textview_yuedujilu:
                Intent intent2 = new Intent(MainActivity.this, YeuDuJiLuActivity.class);
                startActivity(intent2);
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.cehua_textview_fenxiang:
                allShare();
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.cehua_textview_dashang:
                Intent intent3 = new Intent(MainActivity.this, DaShangActivity.class);
                startActivity(intent3);
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
    }





}