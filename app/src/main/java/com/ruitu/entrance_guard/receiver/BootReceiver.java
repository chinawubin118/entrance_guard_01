package com.ruitu.entrance_guard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ruitu.entrance_guard.ui.activity.MainActivity;

/**
 * Created by wubin on 2017/5/10.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Log.d("wubin", "boot start................");

            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //使用Receiver直接启动Activity时候需要加入此flag，否则系统会出现异常
            context.startActivity(i);
        }
    }
}
