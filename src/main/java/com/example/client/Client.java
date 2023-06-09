package com.example.client;


import com.example.Controller.ChatController;
import com.example.Dao.UserDao;
import com.example.Json.ChatMessage;
import com.example.demo.MySQLConnector;
import com.example.entity.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.CloseHandshakeType;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.Controller.ChatController.*;
import static com.example.Controller.GroupChatController.group;
import static com.example.Controller.GroupChatController.groupChatController;
import static javafx.application.Application.launch;

public class Client extends WebSocketClient  {

    public static Client client;
    public String user;
    public Client(URI serverUri,String user) {
        super(serverUri);
        this.user =user;
    }
    public boolean joinGroup=false;

    @Override
    public void onOpen(ServerHandshake handshake) {
        ChatMessage chatMessage = new ChatMessage(user,user, "成功连接到服务器！",null);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(chatMessage);
        send(jsonMessage);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("left");
    }



    @Override
    public void onMessage(String message) {
        if(message.equals("exit")){
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "通知", "服务器已关闭，请勿再发消息！");
            });
        }
        System.out.println(message);
        // 解析收到的 JSON 格式的消息内容为消息对象
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ObservableList.class, new ObservableListDeserializer());
        Gson gson = gsonBuilder.create();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        // 获取发送方、接收方和消息内容
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
        String content = chatMessage.getContent();
        System.out.println(sender);
        if(sender.equals("更新群聊")){
            Platform.runLater(() -> {
                group.addAll(chatMessage.getOnlineUser().stream()
                        .filter(e -> !group.contains(e))
                        .collect(Collectors.toList()));
            });
            return;
        }
        if(sender.equals("退出群聊")){
            Platform.runLater(() -> {
                group.remove(content);
            });
            return;
        }
        if(receiver.equals("群发")){
            if(joinGroup){
                //在群聊窗口中显示消息
                Platform.runLater(() -> {
                    groupChatController.groupChatTextArea.appendText("["+sender+"]:"+   content.replace("\\n","\n")+"\n" );
                });
            }
            return;
        }
        if(sender.equals("退出")) {
            Platform.runLater(() -> {
                // 将收到的消息添加到ObservableList中
                names.remove(content);
            });
            return;
        }
        if(sender.equals("更新")) {
            Platform.runLater(() -> {
                // 将收到的消息添加到ObservableList中
                names.addAll(chatMessage.getOnlineUser().stream()
                        .filter(e -> !names.contains(e))
                        .collect(Collectors.toList()));
            });
        }
        else if(!content.equals("成功连接到服务器！")){
            Platform.runLater(() -> {
                if(chatController.getFriendStage(sender)) {
                    Stage friendStage = chatController.getOrCreateFriendStage(sender);
                    // 在好友聊天窗口中显示消息
                    TextArea friendChatTextArea = (TextArea) friendStage.getScene().lookup("#chatTextArea");
                    friendChatTextArea.appendText("[" + sender + "]:" + content.replace("\\n", "\n") + "\n");
                    friendStage.setTitle(sender + "发来一条新消息！");
                } });

        }

    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Client getClient() {
        return client;
    }

    @Override
    public void onError(Exception ex) {
        // 处理错误时的逻辑
    }

    public static void startClient(String username){
        try {
            client = new Client(new URI("ws://localhost:18882"),username);
            client.connect();
            // 获取用户输入并发送消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while ((input = reader.readLine()) != null) {
                ChatMessage chatMessage = new ChatMessage(username,username, input,null);
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(chatMessage);
                client.send(jsonMessage);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    // 将消息封装成 JSON 格式并发送
    public static void main(String[] args) {
        startClient("hh");
    }
}