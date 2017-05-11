package com.ruitu.entrance_guard.support.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wubin on 2017/5/10.
 */

public class TimeUtils {
    /**
     * 获取当前日期是星期几
     */
    public static String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取当前日期是几号
     */
    public static String getDateNum() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.DATE));
    }

    /**
     * 获取时间(格式 10:01")
     */
    public static String getHourAndMinute() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    /**
     * 获取日期(格式 5.10)
     */
    public static String getDate() {
        return new SimpleDateFormat("M.d").format(new Date());
    }

    /**
     * 获取时间的小时数
     */
    public static String getHourNum() {
        return new SimpleDateFormat("H").format(new Date());
    }
}
