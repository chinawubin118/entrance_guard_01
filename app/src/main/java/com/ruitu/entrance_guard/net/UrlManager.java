package com.ruitu.entrance_guard.net;

/**
 * Created by gaoxiang
 * 服务访问路径
 */

public class UrlManager {

    // 主服务器路径
    private static String HOST_IP = "ceshi.lianxinyi.com.cn:8087";
    //private static String HOST_IP = "192.168.2.112";
    private static String HOST = "http://" + HOST_IP;
    public static String HOST_WS = "ws://" + HOST_IP;

    // 登陆
    public static String USER_LOGIN = HOST + "/app/user/login";

    // 注销
    public static String USER_LOGOUT = HOST + "";


    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        UrlManager.HOST = HOST;
    }
}
