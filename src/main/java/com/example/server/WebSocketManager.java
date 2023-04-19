package com.example.server;

import org.java_websocket.WebSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 定义一个类来管理 WebSocket 连接
public class WebSocketManager {
    private static Map<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    // 添加 WebSocket 连接到管理器
    public static void addWebSocket(String sessionId, WebSocket conn) {
        webSocketMap.put(sessionId, conn);
    }

    // 根据username  获取 WebSocket 连接
    public static WebSocket getWebSocketByUsername(String username) {
        return webSocketMap.get(username);
    }

    // 移除 WebSocket 连接从管理器
    public static void removeWebSocket(String sessionId) {
        webSocketMap.remove(sessionId);
    }

    //遍历
    public static void broadcast(String message){
        for (String key : webSocketMap.keySet()) {
            WebSocket conn = webSocketMap.get(key);
            conn.send("hh");
            System.out.println(conn);
        }
    }
}
