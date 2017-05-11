package com.ruitu.entrance_guard.support.utils;

import com.ruitu.entrance_guard.R;

/**
 * Created by wubin on 2017/5/11.
 */

public class WeatherUtils {

    /**
     * 根据天气现象代码获取天气资源icon
     *
     * @param weatherCode 天气现象代码
     * @return
     */
    public static int getWeatherIcon(int weatherCode) {
        switch (weatherCode) {
            //晴
            case 0:
            case 1:
            case 2:
            case 3:
                return R.drawable.sunny;

            //多云
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return R.drawable.cloudy4;

            //阴
            case 9:
                return R.drawable.overcast;

            //雷阵雨
            case 11:
            case 12:
                return R.drawable.tstorm1;

            //小雨
            case 10:
            case 13:
                return R.drawable.light_rain;

            //中雨
            case 14:
                return R.drawable.shower3;

            //大雨
            case 15:
                //暴雨
            case 16:
            case 17:
            case 18:
                return R.drawable.hail;

            //雨夹雪
            case 19:
            case 20:
                return R.drawable.sleet;

            //小雪
            case 21:
            case 22:
                return R.drawable.snow4;

            //中雪
            case 23:
                //大雪
            case 24:
            case 25:
                return R.drawable.snow5;

            //沙尘暴
            case 26:
            case 27:
            case 28:
            case 29:
                //雾
            case 30:
                //霾
            case 31:
                return R.drawable.fog;

            //风,飓风,热带风暴,龙卷风,热,冷,未知(显示未知天气)
            default:
                return R.drawable.dunno;
        }
    }
}
