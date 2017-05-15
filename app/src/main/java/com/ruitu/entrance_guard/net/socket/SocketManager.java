package com.ruitu.entrance_guard.net.socket;

import org.java_websocket.client.WebSocketClient;

/**
 * Created by Administrator
 */

public class SocketManager {

    public static WebSocketClient webSocket;

    /**
     * 在云端注册
     * @param userId 设备ID
     */
    public static void register(String userId){
        webSocket.send("door,register:" + userId);
    }



}
