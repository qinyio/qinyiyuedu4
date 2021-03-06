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
    //????????????
    private Button mButton_zise_hei, mButton_zise_hui, mButton_zise_bai, mButton_zise_cheng, mButton_zise_lan, mButton_zise_lv;
    //????????????
    private Button mButton_beise_hei, mButton_beise_hui, mButton_beise_bai, mButton_beise_cheng, mButton_beise_lv, mButton_beise_lan;
    //???????????????????????????
    float DownX, DownY, xUp, yUp;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerview_mulu;
    private LinearLayout mLinearlayout_tiaojie;
    private LinearLayout mLinearlayout;
    private SeekBar mSeekBar;
    private String txt = null;
    //????????????,??????,??????
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

        //???????????????url??????????????????????????????????????????????????????
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getExtras().getString("URL");
            shuqian = intent.getExtras().getInt("shuqian");
            name = intent.getExtras().getString("name");
        }
        //?????????????????????????????????
        initViews();

        //??????????????????
//        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo==null || !networkInfo.isAvailable()){
//            System.out.println("??????????????? ");
//        }else {
//            System.out.println("??????????????? ");
//        }
        //??????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                //????????????
                Message message = new Message();
                message.what = 0010;
                handler.sendMessage(message);
                //??????????????????
                initData_ZJ(url);
            }
        }).start();

        //????????????????????????
        liangdu();
        //??????????????????????????????
        zitishezhi();

    }



    private void liangdu() {
        //????????????????????????????????????seekbar??????????????????
        mSeekBar.setMax(BrightnessUtil.getMaxBrightness(YueDuActivity.this));
        //????????????????????????????????????seekbar???????????????
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

        //??????textview?????????
        mTextview_txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        //???TextView?????????????????????
        mTextview_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //????????????
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

        //????????????
        //???????????????
        mTextView_xiayizhang.setOnClickListener(this::onClick);
        //???????????????
        mTextView_shangyizhang.setOnClickListener(this::onClick);
        //????????????,??????????????????,??????????????????????????????
        mTextView_mulu.setOnClickListener(this::onClick);
        //????????????
        mTextView_tiaojie.setOnClickListener(this::onClick);
        //?????????
        mTextView_jiahao.setOnClickListener(this::onClick);
        //?????????
        mTextView_jianhao.setOnClickListener(this::onClick);
        //????????????
        mButton_zise_hei.setOnClickListener(this::onClick);
        mButton_zise_hui.setOnClickListener(this::onClick);
        mButton_zise_bai.setOnClickListener(this::onClick);
        mButton_zise_cheng.setOnClickListener(this::onClick);
        mButton_zise_lv.setOnClickListener(this::onClick);
        mButton_zise_lan.setOnClickListener(this::onClick);
        //????????????
        mButton_beise_hei.setOnClickListener(this::onClick);
        mButton_beise_hui.setOnClickListener(this::onClick);
        mButton_beise_bai.setOnClickListener(this::onClick);
        mButton_beise_cheng.setOnClickListener(this::onClick);
        mButton_beise_lv.setOnClickListener(this::onClick);
        mButton_beise_lan.setOnClickListener(this::onClick);


        //????????????
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* ??????????????????????????????*/
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //???????????????????????????????????????
                BrightnessUtil.SetSystemLight(progress, YueDuActivity.this);
            }

            /*??????????????????????????????*/
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /*?????????????????????????????? */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //??????????????????????????????????????????
    private void downTxt(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HtmlTxt htmlTxt = new HtmlTxt();
                txt = htmlTxt.parseTxt(Data_zj.get(position).getHref_zj());
                //????????????
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
                    Toast.makeText(YueDuActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.yuedu_textview_xiayizhang:
                if (shuqian + 1 < Data_zj.size()) {
                    shuqian = shuqian + 1;
                    downTxt(shuqian);
                    gengxingshujiku();
                } else {
                    Toast.makeText(YueDuActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.yuedu_textview_mulu:
                //?????????????????????
                moveToPosition(shuqian);
                //???????????????
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.yuedu_textview_tiaojie:
                //???????????????????????????
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
                //????????????????????????
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
                //????????????????????????
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

    //????????????
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

    //????????????
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

        //????????????????????????
        mTextView_zihao.setText(daxiao + "");
        //??????????????????
        mTextview_txt.setTextSize((float) daxiao);
        //??????????????????
        mTextview_txt.setTextColor(Color.parseColor(yanse));
        //??????????????????
        mTextview_txt.setBackgroundColor(Color.parseColor(beijing));
    }

    private void initData_ZJ(String url) {
        Html_ZhangJi htmlZhangJi = new Html_ZhangJi();
        Data_zj = htmlZhangJi.parseYueDu(url);
        initData_Txt(Data_zj.get(shuqian).getHref_zj());

        //????????????,???????????????
        Message message = new Message();
        message.what = 0001;
        handler.sendMessage(message);

    }

    private void initData_Txt(String href_zj) {
        HtmlTxt htmlTxt = new HtmlTxt();
        txt = htmlTxt.parseTxt(href_zj);
    }

    private void showPopup() {
        //????????????
        View contentView = LayoutInflater.from(YueDuActivity.this).inflate(R.layout.loading_popup, null);
        popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        //??????popupWindow??????,??????????????????
        popupWindow.setWindowLayoutType(10);
        //????????????????????? ??????????????????
        popupWindow.setOutsideTouchable(false);
        //???????????????  ??????????????? ??????
        popupWindow.setFocusable(true);
        //??????PopupWindow
        View rootview = LayoutInflater.from(YueDuActivity.this).inflate(R.layout.activity_sou_suo, null);
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
        //?????????????????????
        layoutManager = new LinearLayoutManager(YueDuActivity.this);
        mRecyclerview_mulu.setLayoutManager(layoutManager);
        //???????????????
        mMuLu_adapter = new MuLuRecyclerViewAdapter(YueDuActivity.this, Data_zj);
        mRecyclerview_mulu.setAdapter(mMuLu_adapter);

        //???recyclerview????????????????????????
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
                //???????????????
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void showTxt() {
        mTextview_txt.setText("");
        //scrollTo???????????????x??????y????????????
        mTextview_txt.scrollTo(0, 0);
        mTextview_txt.append(txt);
    }
}