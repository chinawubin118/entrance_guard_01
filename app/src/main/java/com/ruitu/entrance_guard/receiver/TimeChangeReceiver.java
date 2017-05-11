package com.ruitu.entrance_guard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ruitu.entrance_guard.support.utils.TimeUtils;

/**
 * Created by wubin on 2017/5/10.
 */

public class TimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_TIME_TICK)) {
            String currentTime = TimeUtils.getHourAndMinute();
            String currentDate = TimeUtils.getDate();
            String currentWeekday = TimeUtils.getWeekOfDate();
            if (null != onTimeChangeListener) {
                onTimeChangeListener.OnTimeChange(currentTime, currentDate, currentWeekday);
            }
        }
    }

    private OnTimeChangeListener onTimeChangeListener;

    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    public interface OnTimeChangeListener {
        void OnTimeChange(String currentTime, String currentDate, String currentWeekday);
    }
}
