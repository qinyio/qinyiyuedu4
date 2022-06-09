package com.example.qinyiyuedu4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qinyiyuedu4.R;

public class ShengMingActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;
    private static String str = "   本软件仅供学习。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheng_ming);


        mTextView = findViewById(R.id.mianze_textview);
        mTextView.setText(str);

        mImageView = findViewById(R.id.mianze_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ShengMingActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }
}