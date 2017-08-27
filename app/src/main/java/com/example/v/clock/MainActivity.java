package com.example.v.clock;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //Tab对应的布局
    private LinearLayout mTab_stopwatch;
    private LinearLayout mTab_timer;
    //Tab对应的Button
    private ImageButton imgbtn_stopwatch;
    private ImageButton imgbtn_timer;
    private TextView tvstopwatch,tvtimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        //将Fragment加入集合中
        mFragments.add(new StopwatchFragment());
        mFragments.add(new TimerFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                //从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                //获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        //设置Tab的点击事件
        mTab_stopwatch.setOnClickListener(this);
        mTab_timer.setOnClickListener(this);
    }

    //初始化控件
    private void initViews() {
        mViewPager = findViewById(R.id.id_viewpager);

        mTab_stopwatch = findViewById(R.id.id_tab_stopwatch);
        mTab_timer = findViewById(R.id.id_tab_timer);

        imgbtn_stopwatch = findViewById(R.id.id_tab_stopwatch_imgbtn);
        imgbtn_timer = findViewById(R.id.id_tab_timer_imgbtn);

        tvstopwatch = findViewById(R.id.id_tab_stopwatch_tv);
        tvtimer = findViewById(R.id.id_tab_timer_tv);
    }

    @Override
    public void onClick(View v) {
        //先将ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_stopwatch:
                selectTab(0);
                break;
            case R.id.id_tab_timer:
                selectTab(1);
                break;
        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                imgbtn_stopwatch.setImageResource(R.drawable.tab_stopwatch_pressed);
                tvstopwatch.setTextColor(Color.parseColor("#0d973e"));
                break;
            case 1:
                imgbtn_timer.setImageResource(R.drawable.tab_count_pressed);
                tvtimer.setTextColor(Color.parseColor("#0d973e"));
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将ImageButton设置为灰色
    private void resetImgs() {
        imgbtn_stopwatch.setImageResource(R.drawable.tab_stopwatch);
        imgbtn_timer.setImageResource(R.drawable.tab_count);
        tvstopwatch.setTextColor(Color.parseColor("#ffffff"));
        tvtimer.setTextColor(Color.parseColor("#ffffff"));
    }

    //按下返回键退向后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

