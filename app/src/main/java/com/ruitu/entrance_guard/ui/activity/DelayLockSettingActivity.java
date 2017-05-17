package com.ruitu.entrance_guard.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.support.utils.KeyUtils;
import com.ruitu.entrance_guard.support.utils.SharedPrefsUtil;
import com.ruitu.entrance_guard.support.utils.UiUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DelayLockSettingActivity extends AppCompatActivity {
    private AppCompatSeekBar seek_bar;
    private TextView tv_delay_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_lock_setting);

        seek_bar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        tv_delay_time = (TextView) findViewById(R.id.tv_delay_time);

        seek_bar.setMax(10);//最大是10
        seek_bar.setProgress(SharedPrefsUtil.getValue(this, "delay_time_progress", 1));

        setTimeText();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String keyStr = KeyUtils.getKeyStrByKeycode(keyCode);

        int currProgress = seek_bar.getProgress();

        if (TextUtils.equals("5", keyStr)) {//确定,关闭当前页面
            SharedPrefsUtil.putValue(this, "delay_time_progress", seek_bar.getProgress());
            onBackPressed();
        }
        if (TextUtils.equals("4", keyStr)) {//相当于音量-
            int newProgress = currProgress - 1;
            seek_bar.setProgress(newProgress <= 0 ? 1 : newProgress);
        } else if (TextUtils.equals("6", keyStr)) {//相当于音量+
            int newProgress = currProgress + 1;
            seek_bar.setProgress(newProgress > 10 ? 10 : newProgress);
        }

        setTimeText();

        return super.onKeyDown(keyCode, event);
    }

    private void setTimeText() {
        Double delayTime = seek_bar.getProgress() * 0.5;
        Double newDouble = changeDouble(delayTime);
        tv_delay_time.setText(newDouble + "秒");
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideBottomUIMenu(this);//不显示底部虚拟按键
    }

    //double 类型保留一位小数
    public double changeDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("0.0");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }
}
