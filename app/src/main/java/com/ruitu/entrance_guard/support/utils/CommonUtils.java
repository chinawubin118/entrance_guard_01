package com.ruitu.entrance_guard.support.utils;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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

    private OnDownloadProgressChange onDownloadProgressChange;

    public void setOnDownloadProgressChange(OnDownloadProgressChange onDownloadProgressChange) {
        this.onDownloadProgressChange = onDownloadProgressChange;
    }

    public interface OnDownloadProgressChange {
        public void onProgressChanged(int currentSize);//当前下载的总大小

        public void onGetFileSize(int fileSize);//文件的大小
    }

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename  本地文件名
     * @throws Exception 各种异常
     */
    public void download(String urlString, String filename) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();

        //回传文件的大小
        if (null != onDownloadProgressChange && null != con) {//
            onDownloadProgressChange.onGetFileSize(con.getContentLength());
        }

        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(filename);

        //下载的总大小
        int downloadSize = 0;
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);

            downloadSize += len;
            if (null != onDownloadProgressChange) {// && downloadSize % (1000 * 1024) == 0
                onDownloadProgressChange.onProgressChanged(downloadSize);
            }
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}
