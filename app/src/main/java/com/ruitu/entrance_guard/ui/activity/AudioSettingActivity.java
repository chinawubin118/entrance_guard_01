package com.ruitu.entrance_guard.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.support.utils.AudioUtil;
import com.ruitu.entrance_guard.support.utils.KeyUtils;
import com.ruitu.entrance_guard.support.utils.UiUtils;

public class AudioSettingActivity extends AppCompatActivity {
    private AppCompatSeekBar seek_bar;

    private AudioUtil audioUtil;
    private int maxVolume;//最大音量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_setting);

        seek_bar = (AppCompatSeekBar) findViewById(R.id.seek_bar);

        audioUtil = AudioUtil.getInstance(this);

        maxVolume = audioUtil.getMediaMaxVolume();
        seek_bar.setMax(maxVolume);
        seek_bar.setProgress(audioUtil.getMediaVolume());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String keyStr = KeyUtils.getKeyStrByKeycode(keyCode);

        if (TextUtils.equals("5", keyStr)) {//确定,关闭当前页面
            onBackPressed();
        }
        if (TextUtils.equals("4", keyStr)) {//相当于音量-
            int currProgress = seek_bar.getProgress();
            int newProgress = (int) (currProgress - 0.1f * maxVolume);
            if (currProgress > 0) {
                seek_bar.setProgress(newProgress);
                audioUtil.setMediaVolume(newProgress);
            } else {
                seek_bar.setProgress(0);
                audioUtil.setMediaVolume(0);
            }
        } else if (TextUtils.equals("6", keyStr)) {//相当于音量+
            int currProgress = seek_bar.getProgress();
            int newProgress = (int) (currProgress + 0.1f * maxVolume);
            if (currProgress < maxVolume) {
                seek_bar.setProgress(newProgress);
                audioUtil.setMediaVolume(newProgress);
            } else {
                seek_bar.setProgress(maxVolume);
                audioUtil.setMediaVolume(maxVolume);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideBottomUIMenu(this);//不显示底部虚拟按键
    }
}
