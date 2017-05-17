package com.ruitu.entrance_guard.support.utils;

import android.content.Context;

import com.beanu.arad.Arad;
import com.ruitu.entrance_guard.R;

import cn.semtec.www.epcontrol.Util;

/**
 * Created by wubin on 2017/5/12.
 */

public class KeyUtils {

    public static final int MAX_LENGTH = 4;//最大按键长度

    public static final int XING_HAO_JIAN = 17;//星号键
    public static final int JING_HAO_JIAN = 18;//井号键
    public static final int DOOR_IS_OPENED = 101;//门开了

    public static long AUTO_LOCK_TIME = getRealDelayTime();//开门后自动锁门时间,测试设置为5s

    private static long getRealDelayTime() {
        int time = 2;
        try {
            time = SharedPrefsUtil.getValue(Arad.app, "delay_time_progress", 1);
        } catch (Exception e) {
            return 2000L;
        }
        return time * 1000L / 2;
    }

    public static String getKeyStrByKeycode(int keyCode) {
        switch (keyCode) {
            case 7:
                return "0";
            case 8:
                return "1";
            case 9:
                return "2";
            case 10:
                return "3";
            case 11:
                return "4";
            case 12:
                return "5";
            case 13:
                return "6";
            case 14:
                return "7";
            case 15:
                return "8";
            case 16:
                return "9";
            case 17:
                return "*";
            case 18:
                return "#";
        }
        return "*";
    }

    public static void playVoiceByKeycode(int keyCode, Context mContext) {
        switch (keyCode) {
            case 7:
                Util.mediaPlay(R.raw.r0, mContext);
                break;
            case 8:
                Util.mediaPlay(R.raw.r1, mContext);
                break;
            case 9:
                Util.mediaPlay(R.raw.r2, mContext);
                break;
            case 10:
                Util.mediaPlay(R.raw.r3, mContext);
                break;
            case 11:
                Util.mediaPlay(R.raw.r4, mContext);
                break;
            case 12:
                Util.mediaPlay(R.raw.r5, mContext);
                break;
            case 13:
                Util.mediaPlay(R.raw.r6, mContext);
                break;
            case 14:
                Util.mediaPlay(R.raw.r7, mContext);
                break;
            case 15:
                Util.mediaPlay(R.raw.r8, mContext);
                break;
            case 16:
                Util.mediaPlay(R.raw.r9, mContext);
                break;
            case 17:
                Util.mediaPlay(R.raw.rkey, mContext);
                break;
            case 18:
                Util.mediaPlay(R.raw.rkey, mContext);
                break;
            case DOOR_IS_OPENED://门开了
                Util.mediaPlay(R.raw.openok, mContext);
                break;
        }
    }
}
