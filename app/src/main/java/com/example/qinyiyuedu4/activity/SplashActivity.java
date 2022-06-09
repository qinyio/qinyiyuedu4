package com.example.qinyiyuedu4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qinyiyuedu4.R;

public class SplashActivity extends AppCompatActivity {

    private TextView mTextView_tiaoguo;
    private TextView mTextView_time;
    //显示广告页面的时间，3 秒
    long showTime = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();

        //延迟3000ms跳转到主页面
        handler.postDelayed(myRunnable, showTime * 1000);
        handler.sendEmptyMessage(111);//給Handler对象发送信息
    }


    //创建Runnable对象
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            jundToMainActivity();
        }
    };

    //跳转到主页的方法，并关闭自身页面
    public void jundToMainActivity() {
        handler.removeCallbacks(myRunnable);//移出Runnable对象
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    //创建Handler对象
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                Log.e("TAG", "what" + showTime);
                mTextView_time.setText("" + showTime);
                showTime--;//时间减一秒
                if (showTime > 0) {
                    //一秒后給自己发送一个信息
                    handler.sendEmptyMessageDelayed(111, 1000);
                }

            }

        }
    };

    private void initViews() {
        mTextView_tiaoguo = findViewById(R.id.splash_textview_tiaoguo);
        mTextView_time = findViewById(R.id.splash_textview_time);
        mTextView_tiaoguo.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_textview_tiaoguo:
                jundToMainActivity();
                break;
        }
    }
}