package com.example.server;


import com.example.Json.ChatMessage;
import com.example.client.ObservableListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;


import static com.example.server.WebSocketManager.getWebSocketByUsername;


public class SocketServer extends WebSocketServer {
    private static int onlineCount = 0; // 记录连接数目
    private static ObservableList<String> onlineUser = FXCollections.observableArrayList(); //在线用户
    private static ObservableList<String> groupUser = FXCollections.observableArrayList();

    public SocketServer(int port) {
        super(new InetSocketAddress(port));
    }





    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        onlineCount++;
        //  获取username
        conn.send("Welcome to the server!"); // This method sends a message to the new client
        broadcast("new connection: " + handshake
                .getResourceDescriptor()); // This method sends a message to all clients connected
        // 在连接建立后，将当前 WebSocket 添加到 WebSocket 管理器中，并关联对应的 Session ID
        System.out.println(
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        // 解析收到的 JSON 格式的消息内容为消息对象
        Gson gson = new GsonBuilder().registerTypeAdapter(ObservableList.class, new ObservableListAdapter()).create();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        System.out.println(message);
        // 获取发送方、接收方和消息内容
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
        String content = chatMessage.getContent();
        if(receiver.equals("退出")){
            onlineUser.remove(sender);
            ChatMessage Message =new ChatMessage("退出","",sender,onlineUser);
            Gson gson1 = new GsonBuilder().registerTypeAdapter(ObservableList.class, new ObservableListAdapter()).create();
            String jsonMessage = gson1.toJson(Message);
            broadcast(jsonMessage);
            return;
        }
        if(receiver.equals("群发")){
            if(content.equals("加入聊天")){
                groupUser.add(sender);
                ChatMessage Message =new ChatMessage("更新群聊","","",groupUser);
                Gson gson1 = new GsonBuilder().registerTypeAdapter(ObservableList.class, new ObservableListAdapter()).create();
                String jsonMessage = gson1.toJson(Message);
                broadcast(jsonMessage);
            }
            else {
                broadcast(message);
            }
            if(content.equals("退出聊天")){
                groupUser.remove(sender);
                ChatMessage Message =new ChatMessage("退出群聊","",sender,groupUser);
                Gson gson1 = new GsonBuilder().registerTypeAdapter(ObservableList.class, new ObservableListAdapter()).create();
                String jsonMessage = gson1.toJson(Message);
                broadcast(jsonMessage);
            }

        }
        if(content.equals("成功连接到服务器！")){
            WebSocketManager.addWebSocket(sender,conn);
            //通知所有用户新用户上线
            onlineUser.add(sender);
            broadcast(sender+"上线了");
            ChatMessage Message =new ChatMessage("更新","","",onlineUser);
            Gson gson1 = new GsonBuilder().registerTypeAdapter(ObservableList.class, new ObservableListAdapter()).create();
            String jsonMessage = gson1.toJson(Message);
            broadcast(jsonMessage);
            conn.send(jsonMessage);
        }
        // 查找目标接收方的 WebSocket 连接
        try {
            WebSocket targetConn = getWebSocketByUsername(receiver);
            // 如果目标接收方的 WebSocket 连接存在，则发送消息给目标接收方
            targetConn.send(message);
        }
        catch(NullPointerException e) {
            System.out.println("目标接收方的 WebSocket 连接不存在");
        }
    }



    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific
            // websocket
        }

    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }


}

