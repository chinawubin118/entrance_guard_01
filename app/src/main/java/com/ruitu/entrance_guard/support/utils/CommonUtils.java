package com.ruitu.entrance_guard.support.utils;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Created by wubin on 2017/5/13.
 */

public class CommonUtils {
    private static String cmd_install = "pm install -r ";
    private static String cmd_uninstall = "pm uninstall ";

    //执行shell命令
    public static int excuteSuCMD(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(
                    (OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入library path可解决该问题
            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            int result = process.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }
}
