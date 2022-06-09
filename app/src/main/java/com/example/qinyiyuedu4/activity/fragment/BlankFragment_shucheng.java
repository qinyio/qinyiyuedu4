package com.example.qinyiyuedu4.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qinyiyuedu4.R;
import com.example.qinyiyuedu4.adapter.FragmentAdapter;

import java.util.ArrayList;


public class BlankFragment_shucheng extends Fragment implements View.OnClickListener{


    private View rootView;
    private TextView mTextview;
    private ViewPager2 mViewpager;
    private FragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragments;
    private TextView mTextview_xuanhuan;
    private TextView mTextview_qihuan;
    private TextView mTextview_wuxia;
    private TextView mTextview_dushi;
    private TextView mTextview_lishi;
    private TextView mTextview_junshi;
    private TextView mTextview_youxi;
    private TextView mTextview_kehuan;
    private static String[] keywords = {"chn21", "chn1", "chn2", "chn4", "chn5", "chn6", "chn7", "chn9"};



    public BlankFragment_shucheng() {
    }

    public static BlankFragment_shucheng newInstance(String param1, String param2) {
        BlankFragment_shucheng fragment = new BlankFragment_shucheng();
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
            rootView = inflater.inflate(R.layout.fragment_blank_shucheng, container, false);
        }

        //获取fragment的控件
        initViews();
        //fragment填充viewpager
        initPager();
        return rootView;
    }



    private void initPager() {

        fragments = new ArrayList<>();
        fragments.add(new BlankFragment_shucheng_2(keywords[0]));
        fragments.add(new BlankFragment_shucheng_2(keywords[1]));
        fragments.add(new BlankFragment_shucheng_2(keywords[2]));
        fragments.add(new BlankFragment_shucheng_2(keywords[3]));
        fragments.add(new BlankFragment_shucheng_2(keywords[4]));
        fragments.add(new BlankFragment_shucheng_2(keywords[5]));
        fragments.add(new BlankFragment_shucheng_2(keywords[6]));
        fragments.add(new BlankFragment_shucheng_2(keywords[7]));

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle(), fragments);
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
            case 0:
                mTextview_xuanhuan.setSelected(true);
                mTextview = mTextview_xuanhuan;

                break;
            case 1:
                mTextview_qihuan.setSelected(true);
                mTextview = mTextview_qihuan;
                break;

            case 2:
                mTextview_wuxia.setSelected(true);
                mTextview = mTextview_wuxia;
                break;

            case 3:
                mTextview_dushi.setSelected(true);
                mTextview = mTextview_dushi;
                break;

            case 4:
                mTextview_lishi.setSelected(true);
                mTextview = mTextview_lishi;
                break;

            case 5:
                mTextview_junshi.setSelected(true);
                mTextview = mTextview_junshi;
                break;

            case 6:
                mTextview_youxi.setSelected(true);
                mTextview = mTextview_youxi;
                break;

            case 7:
                mTextview_kehuan.setSelected(true);
                mTextview = mTextview_kehuan;
                break;
        }
    }


    private void initViews() {
        mViewpager = rootView.findViewById(R.id.shucheng_viewpager);
        mTextview_xuanhuan = rootView.findViewById(R.id.shucheng_textview_xuanhuan);
        mTextview_qihuan = rootView.findViewById(R.id.shucheng_textview_qihuan);
        mTextview_wuxia = rootView.findViewById(R.id.shucheng_textview_wuxia);
        mTextview_dushi = rootView.findViewById(R.id.shucheng_textview_dushi);
        mTextview_lishi = rootView.findViewById(R.id.shucheng_textview_lishi);
        mTextview_junshi = rootView.findViewById(R.id.shucheng_textview_junshi);
        mTextview_youxi = rootView.findViewById(R.id.shucheng_textview_youxi);
        mTextview_kehuan = rootView.findViewById(R.id.shucheng_textview_kehuan);

        mTextview_xuanhuan.setOnClickListener(this::onClick);
        mTextview_qihuan.setOnClickListener(this::onClick);
        mTextview_wuxia.setOnClickListener(this::onClick);
        mTextview_dushi.setOnClickListener(this::onClick);
        mTextview_lishi.setOnClickListener(this::onClick);
        mTextview_junshi.setOnClickListener(this::onClick);
        mTextview_youxi.setOnClickListener(this::onClick);
        mTextview_kehuan.setOnClickListener(this::onClick);

        //默认焦点
        mTextview_xuanhuan.setSelected(true);
        mTextview = mTextview_xuanhuan;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shucheng_textview_xuanhuan:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.shucheng_textview_qihuan:
                mViewpager.setCurrentItem(1);
                break;
            case R.id.shucheng_textview_wuxia:
                mViewpager.setCurrentItem(2);
                break;
            case R.id.shucheng_textview_dushi:
                mViewpager.setCurrentItem(3);
                break;
            case R.id.shucheng_textview_lishi:
                mViewpager.setCurrentItem(4);
                break;
            case R.id.shucheng_textview_junshi:
                mViewpager.setCurrentItem(5);
                break;
            case R.id.shucheng_textview_youxi:
                mViewpager.setCurrentItem(6);
                break;
            case R.id.shucheng_textview_kehuan:
                mViewpager.setCurrentItem(7);
                break;
        }

    }
}