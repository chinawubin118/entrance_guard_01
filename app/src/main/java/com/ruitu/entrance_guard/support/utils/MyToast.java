package com.ruitu.entrance_guard.support.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruitu.entrance_guard.R;

/**
 * Created by Lotte on 2016/10/8.
 * Toast工具,不创建新的Toast对象(可以直接修改Toast上面的文字)
 */
public class MyToast {
    private static Toast mToast;
    private static View mToastView;

    /**
     * 显示短时间的Toast
     */
    public static void showShortToast(Context context, String msg) {
        if (mToast == null) {
            mToast = new Toast(context);
        }

        if (mToastView == null) {
            mToastView = LayoutInflater.from(context).inflate(R.layout.toast_hint_layout, null);
        }

        TextView tv_msg = (TextView) mToastView.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        mToast.setView(mToastView);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);

        mToast.show();
    }

    public static void showLongToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
