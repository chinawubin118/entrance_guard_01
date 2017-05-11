package com.ruitu.entrance_guard.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruitu.entrance_guard.Constant;
import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.impl.PageChangeListenerImpl;
import com.ruitu.entrance_guard.receiver.TimeChangeReceiver;
import com.ruitu.entrance_guard.support.utils.AnimationUtils;
import com.ruitu.entrance_guard.support.utils.TimeUtils;
import com.ruitu.entrance_guard.support.utils.UiUtils;
import com.ruitu.entrance_guard.ui.adapter.AdAdapter;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimeChangeReceiver.OnTimeChangeListener {

    private ViewPager vp_ad;//轮播广告
    private RelativeLayout rl_bg;//弹出来的公告(包含文字和背景)
    private TextView tv_state, tv_time, tv_date, tv_weekday;//在线状态,当前时间,日期,星期几
    private View ad_content, dial_content;//广告和公告的布局,拨号时的布局
    private EditText et_dial_num;//显示摁下的号码

    //广告图片
    private int[] imgResIds = {R.mipmap.img_01, R.mipmap.img_02, R.mipmap.img_03};
    //当前选中的图片的position
    private int currPosition;

    //接收时间改变的receiver
    private TimeChangeReceiver timeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化view控件
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        et_dial_num = (EditText) findViewById(R.id.et_dial_num);
        ad_content = findViewById(R.id.ad_content);
        dial_content = findViewById(R.id.dial_content);

        initAndRegisterReceiver();

        vp_ad.setAdapter(new AdAdapter(this, imgResIds));

        addListener();//派发事件监听
        executeLunbo();
    }

    //初始化并注册timeReceiver
    private void initAndRegisterReceiver() {
        timeReceiver = new TimeChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filter);
    }

    //页面改变监听
    private void addListener() {
        vp_ad.addOnPageChangeListener(new PageChangeListenerImpl() {
            @Override
            public void onPageSelected(int position) {
                setFocus();
                currPosition = position;
            }
        });
        rl_bg.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        timeReceiver.setOnTimeChangeListener(this);
    }

    //执行广告的轮播
    private void executeLunbo() {
        handler.sendEmptyMessageDelayed(1001, Constant.TIME_UNIT);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            currPosition++;
            if (currPosition >= imgResIds.length) {
                currPosition = 0;
            }
            vp_ad.setCurrentItem(currPosition);
            sendEmptyMessageDelayed(1001, Constant.TIME_UNIT);
        }
    };

    private void setFocus() {
        vp_ad.setFocusable(false);
        vp_ad.setFocusableInTouchMode(false);
    }

    @Override
    public void onClick(View v) {
        if (v == rl_bg) {//点击弹出来的公告
            hideNotice();
        }
        if (v == tv_state) {
            showNotice();
        }
        if (v == tv_time) {
            et_dial_num.setCursorVisible(false);//光标隐藏，提升用户的体验度

            String oldStr = et_dial_num.getText().toString();
            ad_content.setVisibility(View.GONE);
            dial_content.setVisibility(View.VISIBLE);//显示拨号的布局

            if (oldStr.length() < 6) {
                StringBuilder sb = new StringBuilder(oldStr);
                String tempNum = new Random().nextInt(9) + "";
                et_dial_num.setText(sb.append(tempNum).toString());
            }
        }
        if (v == tv_date) {//s = s.Substring(0,s.Length - 1)
            et_dial_num.setCursorVisible(false);//光标隐藏，提升用户的体验度

            String oldStr = et_dial_num.getText().toString();
            if (!TextUtils.isEmpty(oldStr)) {
                String newStr = oldStr.substring(0, oldStr.length() - 1);
                et_dial_num.setText(newStr);
            }

            if (oldStr.length() > 1) {

            } else {
                ad_content.setVisibility(View.VISIBLE);
                dial_content.setVisibility(View.GONE);//显示广告的布局
            }
        }
    }

    @Override
    public void OnTimeChange(String currentTime, String currentDate, String currentWeekday) {
        setTimeToView(currentTime, currentDate, currentWeekday);
    }

    //为视图设置时间
    private void setTimeToView(String time, String date, String weekday) {
        tv_time.setText(time);
        tv_date.setText(date);
        tv_weekday.setText(weekday);
    }

    //显示公告
    private void showNotice() {
        if (rl_bg.getVisibility() != View.VISIBLE) {
            rl_bg.setVisibility(View.VISIBLE);
            AnimationUtils.AlphaAnimation(rl_bg, 0f, 1f, 200);
        }
    }

    //隐藏公告
    private void hideNotice() {
        if (rl_bg.getVisibility() != View.GONE) {
            rl_bg.setVisibility(View.GONE);
            AnimationUtils.AlphaAnimation(rl_bg, 1f, 0f, 200);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideBottomUIMenu(this);//不显示底部虚拟按键
        setTimeToView(TimeUtils.getHourAndMinute(), TimeUtils.getDate(), TimeUtils.getWeekOfDate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
    }
}
