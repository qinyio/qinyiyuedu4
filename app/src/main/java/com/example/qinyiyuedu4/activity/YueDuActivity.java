package com.example.qinyiyuedu4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.adapter.MuLuRecyclerViewAdapter;
import com.example.qinyiyuedu4.html.HtmlTxt;
import com.example.qinyiyuedu4.html.Html_ZhangJi;
import com.example.qinyiyuedu4.pojo.Book;
import com.example.qinyiyuedu4.pojo.Content_zj;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_shu_ji_ji_lu;
import com.example.qinyiyuedu4.sqlite.DatabaseHelper_ziti;
import com.example.qinyiyuedu4.util.BrightnessUtil;
import com.example.qinyiyuedu4.util.LinearTopSmoothScroller;

import java.util.ArrayList;

public class YueDuActivity extends AppCompatActivity {

    private String url;
    private int shuqian;
    private PopupWindow popupWindow;
    private ArrayList<Content_zj> Data_zj;

    private TextView mTextview_txt, mTextView_mulu, mTextView_shangyizhang, mTextView_xiayizhang, mTextView_tiaojie, mTextView_jiahao, mTextView_jianhao, mTextView_zihao;
    //字体颜色
    private Button mButton_zise_hei, mButton_zise_hui, mButton_zise_bai, mButton_zise_cheng, mButton_zise_lan, mButton_zise_lv;
    //背景颜色
    private Button mButton_beise_hei, mButton_beise_hui, mButton_beise_bai, mButton_beise_cheng, mButton_beise_lv, mButton_beise_lan;
    //用来区分点击和滑动
    float DownX, DownY, xUp, yUp;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerview_mulu;
    private LinearLayout mLinearlayout_tiaojie;
    private LinearLayout mLinearlayout;
    private SeekBar mSeekBar;
    private String txt = null;
    //字体大小,颜色,背景
    private int daxiao;
    private String yanse;
    private String beijing;

    private SQLiteDatabase db_ziti;
    private DatabaseHelper_ziti databaseHelper_ziti;
    private SQLiteDatabase db_shu_ji;
    private DatabaseHelper_shu_ji databaseHelper_shu_ji;
    private DatabaseHelper_shu_ji_ji_lu databaseHelperShuJiJilu;
    private SQLiteDatabase db_shu_ji_ji_lu;
    private String name;

    private MuLuRecyclerViewAdapter mMuLu_adapter;
    private LinearLayoutManager layoutManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_du);

        //获取传来的url，用来获取章节信息。书签定位第几章。
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getExtras().getString("URL");
            shuqian = intent.getExtras().getInt("shuqian");
            name = intent.getExtras().getString("name");
        }
        //绑定控件并处理点击事件
        initViews();

        //判断联网状态
//        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo==null || !networkInfo.isAvailable()){
//            System.out.println("有可用网络 ");
//        }else {
//            System.out.println("无可用网络 ");
//        }
        //获取章节信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                //信息传递
                Message message = new Message();
                message.what = 0010;
                handler.sendMessage(message);
                //获取章节信息
                initData_ZJ(url);
            }
        }).start();

        //设置当前屏幕亮度
        liangdu();
        //设置初始字体大小颜色
        zitishezhi();

    }



    private void liangdu() {
        //将系统最大屏幕亮度值设为seekbar的最大进度值
        mSeekBar.setMax(BrightnessUtil.getMaxBrightness(YueDuActivity.this));
        //将系统当前屏幕亮度值设为seekbar当前进度值
        mSeekBar.setProgress(BrightnessUtil.getBrightness(YueDuActivity.this));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mTextview_txt = findViewById(R.id.yuedu_textview_txt);
        mLinearlayout = findViewById(R.id.yuedu_linearlayout);
        mTextView_mulu = findViewById(R.id.yuedu_textview_mulu);
        mTextView_shangyizhang = findViewById(R.id.yuedu_textview_shangyizhang);
        mTextView_xiayizhang = findViewById(R.id.yuedu_textview_xiayizhang);
        mTextView_tiaojie = findViewById(R.id.yuedu_textview_tiaojie);
        mDrawerLayout = findViewById(R.id.yuedu_DrawerLayout);
        mRecyclerview_mulu = findViewById(R.id.yuedu_cehua_recyclerview);
        mLinearlayout_tiaojie = findViewById(R.id.yuedu_linearlayout_tiaojie);
        mTextView_jiahao = findViewById(R.id.yuedu_textview_jia);
        mTextView_jianhao = findViewById(R.id.yuedu_textview_jian);
        mTextView_zihao = findViewById(R.id.yuedu_textview_zihao);
        mSeekBar = findViewById(R.id.yuedu_seekbar_liangdu);

        mButton_zise_hei = findViewById(R.id.yuedu_button_zise_hei);
        mButton_zise_hui = findViewById(R.id.yuedu_button_zise_hui);
        mButton_zise_bai = findViewById(R.id.yuedu_button_zise_bai);
        mButton_zise_cheng = findViewById(R.id.yuedu_button_zise_cheng);
        mButton_zise_lv = findViewById(R.id.yuedu_button_zise_lv);
        mButton_zise_lan = findViewById(R.id.yuedu_button_zise_lan);

        mButton_beise_hei = findViewById(R.id.yuedu_button_beise_hei);
        mButton_beise_hui = findViewById(R.id.yuedu_button_beise_hui);
        mButton_beise_bai = findViewById(R.id.yuedu_button_beise_bai);
        mButton_beise_cheng = findViewById(R.id.yuedu_button_beise_cheng);
        mButton_beise_lv = findViewById(R.id.yuedu_button_beise_lv);
        mButton_beise_lan = findViewById(R.id.yuedu_button_beise_lan);

        //设置textview可滚动
        mTextview_txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        //对TextView点击和滑动区分
        mTextview_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当按下时
                    DownX = event.getX();
                    DownY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    xUp = event.getX();
                    yUp = event.getY();

                    if ((Math.abs(xUp - DownX) < 20) && (Math.abs(yUp - DownY) < 20)) {
                        if (mLinearlayout.getVisibility() == View.GONE) {
                            mLinearlayout.setVisibility(View.VISIBLE);
                            mLinearlayout_tiaojie.setVisibility(View.GONE);
                        } else if (mLinearlayout.getVisibility() == View.VISIBLE) {
                            mLinearlayout.setVisibility(View.GONE);
                            mLinearlayout_tiaojie.setVisibility(View.GONE);
                        }
                    }
                }
                return false;
            }
        });

        //点击事件
        //点击下一章
        mTextView_xiayizhang.setOnClickListener(this::onClick);
        //点击上一章
        mTextView_shangyizhang.setOnClickListener(this::onClick);
        //点击目录,弹出目录界面,并把信息传到目录界面
        mTextView_mulu.setOnClickListener(this::onClick);
        //点击调节
        mTextView_tiaojie.setOnClickListener(this::onClick);
        //字体加
        mTextView_jiahao.setOnClickListener(this::onClick);
        //字体减
        mTextView_jianhao.setOnClickListener(this::onClick);
        //字体颜色
        mButton_zise_hei.setOnClickListener(this::onClick);
        mButton_zise_hui.setOnClickListener(this::onClick);
        mButton_zise_bai.setOnClickListener(this::onClick);
        mButton_zise_cheng.setOnClickListener(this::onClick);
        mButton_zise_lv.setOnClickListener(this::onClick);
        mButton_zise_lan.setOnClickListener(this::onClick);
        //背景颜色
        mButton_beise_hei.setOnClickListener(this::onClick);
        mButton_beise_hui.setOnClickListener(this::onClick);
        mButton_beise_bai.setOnClickListener(this::onClick);
        mButton_beise_cheng.setOnClickListener(this::onClick);
        mButton_beise_lv.setOnClickListener(this::onClick);
        mButton_beise_lan.setOnClickListener(this::onClick);


        //调节亮度
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* 拖动条进度改变时调用*/
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //当前屏幕亮度随着进度值改变
                BrightnessUtil.SetSystemLight(progress, YueDuActivity.this);
            }

            /*拖动条开始拖动时调用*/
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /*拖动条停止拖动时调用 */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //获取对应章节内容，更新数据库
    private void downTxt(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HtmlTxt htmlTxt = new HtmlTxt();
                txt = htmlTxt.parseTxt(Data_zj.get(position).getHref_zj());
                //信息传递
                Message message = new Message();
                message.what = 0011;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.yuedu_textview_shangyizhang:
                if (shuqian > 0) {
                    shuqian = shuqian - 1;
                    downTxt(shuqian);
                    gengxingshujiku();
                } else {
                    Toast.makeText(YueDuActivity.this, "没有上一章了", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.yuedu_textview_xiayizhang:
                if (shuqian + 1 < Data_zj.size()) {
                    shuqian = shuqian + 1;
                    downTxt(shuqian);
                    gengxingshujiku();
                } else {
                    Toast.makeText(YueDuActivity.this, "这是最后一章", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.yuedu_textview_mulu:
                //定位到当前章节
                moveToPosition(shuqian);
                //打开侧滑栏
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.yuedu_textview_tiaojie:
                //调节控件隐藏，现行
                if (mLinearlayout_tiaojie.getVisibility() == View.GONE) {
                    mLinearlayout_tiaojie.setVisibility(View.VISIBLE);
                } else {
                    mLinearlayout_tiaojie.setVisibility(View.GONE);
                }
                break;
            case R.id.yuedu_textview_jia:
                databaseHelper_ziti = new DatabaseHelper_ziti(getApplication());
                db_ziti = databaseHelper_ziti.getWritableDatabase();
                daxiao = daxiao + 1;
                //显示当前自号大小
                mTextView_zihao.setText(daxiao + "");
                ContentValues values_jia = new ContentValues();
                values_jia.put("daxiao", daxiao);
                db_ziti.update(Book.TABLE_NAME_ZI_TI, values_jia, null, null);
                db_ziti.close();
                databaseHelper_ziti.close();
                zitishezhi();
                break;
            case R.id.yuedu_textview_jian:
                databaseHelper_ziti = new DatabaseHelper_ziti(getApplication());
                db_ziti = databaseHelper_ziti.getWritableDatabase();
                daxiao = daxiao - 1;
                //显示当前自号大小
                mTextView_zihao.setText(daxiao + "");
                ContentValues values_jian = new ContentValues();
                values_jian.put("daxiao", daxiao);
                db_ziti.update(Book.TABLE_NAME_ZI_TI, values_jian, null, null);
                db_ziti.close();
                databaseHelper_ziti.close();
                zitishezhi();
                break;
            case R.id.yuedu_button_zise_hei:
                zise(Book.YAN_SE_HEI);
                break;
            case R.id.yuedu_button_zise_hui:
                zise(Book.YAN_SE_HUI);
                break;
            case R.id.yuedu_button_zise_bai:
                zise(Book.YAN_SE_BAI);
                break;
            case R.id.yuedu_button_zise_cheng:
                zise(Book.YAN_SE_CHENG);
                break;
            case R.id.yuedu_button_zise_lv:
                zise(Book.YAN_SE_LV);
                break;
            case R.id.yuedu_button_zise_lan:
                zise(Book.YAN_SE_LAN);
                break;
            case R.id.yuedu_button_beise_hei:
                beise(Book.YAN_SE_HEI);
                break;
            case R.id.yuedu_button_beise_hui:
                beise(Book.YAN_SE_HUI);
                break;
            case R.id.yuedu_button_beise_bai:
                beise(Book.YAN_SE_BAI);
                break;
            case R.id.yuedu_button_beise_cheng:
                beise(Book.YAN_SE_CHENG);
                break;
            case R.id.yuedu_button_beise_lv:
                beise(Book.YAN_SE_LV);
                break;
            case R.id.yuedu_button_beise_lan:
                beise(Book.YAN_SE_LAN);
                break;
        }
    }

    private void gengxingshujiku() {

        ContentValues values = new ContentValues();
        values.put("shuqian", shuqian);
        values.put("duzhi", Data_zj.get(shuqian).getName_zj());

        databaseHelper_shu_ji = new DatabaseHelper_shu_ji(getApplication());
        db_shu_ji = databaseHelper_shu_ji.getWritableDatabase();
        db_shu_ji.update(Book.TABLE_NAME_SHU_JI, values, "name=?", new String[]{name});
        db_shu_ji.close();
        databaseHelper_shu_ji.close();

        databaseHelperShuJiJilu = new DatabaseHelper_shu_ji_ji_lu(getApplication());
        db_shu_ji_ji_lu = databaseHelperShuJiJilu.getWritableDatabase();
        db_shu_ji_ji_lu.update(Book.TABLE_NAME_SHU_JI_JI_LU, values, "name=?", new String[]{name});
        db_shu_ji_ji_lu.close();
        databaseHelperShuJiJilu.close();
    }

    //背景颜色
    private void beise(String beise) {
        databaseHelper_ziti = new DatabaseHelper_ziti(getApplication());
        db_ziti = databaseHelper_ziti.getWritableDatabase();
        ContentValues values_jian = new ContentValues();
        values_jian.put("beijing", beise);
        db_ziti.update(Book.TABLE_NAME_ZI_TI, values_jian, null, null);
        db_ziti.close();
        databaseHelper_ziti.close();
        zitishezhi();
    }

    //字体颜色
    private void zise(String zise) {
        databaseHelper_ziti = new DatabaseHelper_ziti(getApplication());
        db_ziti = databaseHelper_ziti.getWritableDatabase();
        ContentValues values_jian = new ContentValues();
        values_jian.put("yanse", zise);
        db_ziti.update(Book.TABLE_NAME_ZI_TI, values_jian, null, null);
        db_ziti.close();
        databaseHelper_ziti.close();
        zitishezhi();
    }

    private void zitishezhi() {
        databaseHelper_ziti = new DatabaseHelper_ziti(YueDuActivity.this);
        db_ziti = databaseHelper_ziti.getWritableDatabase();
        Cursor cursor = db_ziti.query(Book.TABLE_NAME_ZI_TI, null, null, null, null, null, null);
        cursor.moveToNext();
        daxiao = cursor.getInt(0);
        yanse = cursor.getString(1);
        beijing = cursor.getString(2);

        cursor.close();
        db_ziti.close();
        databaseHelper_ziti.close();

        //显示当前自号大小
        mTextView_zihao.setText(daxiao + "");
        //设置字体大小
        mTextview_txt.setTextSize((float) daxiao);
        //设置字体颜色
        mTextview_txt.setTextColor(Color.parseColor(yanse));
        //设置背景颜色
        mTextview_txt.setBackgroundColor(Color.parseColor(beijing));
    }

    private void initData_ZJ(String url) {
        Html_ZhangJi htmlZhangJi = new Html_ZhangJi();
        Data_zj = htmlZhangJi.parseYueDu(url);
        initData_Txt(Data_zj.get(shuqian).getHref_zj());

        //加载完成,让转圈停止
        Message message = new Message();
        message.what = 0001;
        handler.sendMessage(message);

    }

    private void initData_Txt(String href_zj) {
        HtmlTxt htmlTxt = new HtmlTxt();
        txt = htmlTxt.parseTxt(href_zj);
    }

    private void showPopup() {
        //设置弹窗
        View contentView = LayoutInflater.from(YueDuActivity.this).inflate(R.layout.loading_popup, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //设置popupWindow层级,越小越在上层
        popupWindow.setWindowLayoutType(10);
        //外部是否可点击 点击外部消失
        popupWindow.setOutsideTouchable(false);
        //响应返回键  点击返回键 消失
        popupWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(YueDuActivity.this).inflate(R.layout.activity_sou_suo, null);
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
                    case 0010:
                        showPopup();
                        break;
                    case 0001:
                        popupWindow.dismiss();
                        showMuLu();
                        showTxt();
                        break;
                    case 0011:
                        showTxt();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void showMuLu() {
        //创建布局管理器
        layoutManager = new LinearLayoutManager(YueDuActivity.this);
        mRecyclerview_mulu.setLayoutManager(layoutManager);
        //创建适配器
        mMuLu_adapter = new MuLuRecyclerViewAdapter(YueDuActivity.this, Data_zj);
        mRecyclerview_mulu.setAdapter(mMuLu_adapter);

        //对recyclerview条目创建点击事件
        initListener();
    }

    private void moveToPosition(int shuqian) {
        LinearTopSmoothScroller smoothScroller = new LinearTopSmoothScroller(getApplication(), false);
        smoothScroller.setTargetPosition(shuqian);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    private void initListener() {
        mMuLu_adapter.setRecyclerItemClickListener(new MuLuRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                shuqian = position;
                downTxt(shuqian);
                gengxingshujiku();
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void showTxt() {
        mTextview_txt.setText("");
        //scrollTo指定滚动到x轴和y轴的位置
        mTextview_txt.scrollTo(0, 0);
        mTextview_txt.append(txt);
    }
}