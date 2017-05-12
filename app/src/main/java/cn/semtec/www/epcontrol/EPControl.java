package cn.semtec.www.epcontrol;

import android.util.Log;

public class EPControl {
    //加载so文件
    static {
        try {
            System.loadLibrary("EPControl");
        } catch (Throwable e) {
            Log.i("wubin", "加载异常.....");
            e.printStackTrace();
        }
    }

    //门口机控制接口API
    public static native int EpControlConnect();      //调用其他函数时要先连接

    public static native void EpControlDisconnect();  //退出后请关闭

    public static native int EpControlUnlock();       //关门

    public static native int EpControlLock();         //开门

    public static native int GetLockStatus();  // 返回 1，门关；0，门开。

    public static native int EPSetLED(int On);//开关灯，1开;0关

    public static native int GetLightSensorStatus();//获取灯的状态，0亮;1灭

    public static native int GetUnLockStatus();   // 返回 － 1，开门按钮未被按下；0，开门按钮被按下；

    public static native int GetTamperStatus();   // 返回 － 1，防拆触发；0，防拆未触发。

    public static native int EPSet485(int On);  //On=1 RS485发送， On=0 RS485 接收

}
