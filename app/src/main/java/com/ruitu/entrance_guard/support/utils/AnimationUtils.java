package com.ruitu.entrance_guard.support.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by wubin on 2017/5/10.
 */
public class AnimationUtils {
    /**
     * 执行渐入 渐出动画效果
     *
     * @param v        需要执行动画的布局
     * @param start    开始时的可见程度
     * @param duration 动画时长 单位:ms
     */
    public static void AlphaAnimation(View v, float start, float end, int duration) {
        Animation a = new AlphaAnimation(start, end);
        a.setDuration(duration);
        v.startAnimation(a);
    }

    /**
     * 位移动画
     */
    public static void TranslateAnimation1(View view, long duration) {
        Animation translateIn = new TranslateAnimation(0, -5, 0, 0);
        translateIn.setDuration(duration);
        translateIn.setFillAfter(true);
        translateIn.setRepeatCount(5);
        translateIn.setRepeatMode(Animation.REVERSE);
        view.startAnimation(translateIn);
    }

    /**
     * 位移动画
     */
    public static void TranslateAnimation(View view, float fromX, float toX, float fromY, float toY, long duration) {
        Animation translateIn = new TranslateAnimation(fromX, toX, fromY, toY);
        translateIn.setDuration(duration);
        translateIn.setFillAfter(true);
        view.startAnimation(translateIn);
    }

    /**
     * 拉伸动画
     */
    public static void ScaleAnimation(View view, float fromX, float toX, float fromY, float toY, int duration, int type) {
        //float fromX, float toX, float fromY
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }
}
