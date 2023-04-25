package com.example.Controller;

import com.example.Json.ChatMessage;
import com.example.client.Client;
import com.example.demo.HelloApplication;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.Controller.FriendChatController.friendChatController;
import static com.example.Controller.GroupChatController.group;
import static com.example.Controller.GroupChatController.groupChatController;
import static java.lang.Thread.sleep;


public class ChatController {

    @FXML
    public ListView<String> friendsListView;


    public static ChatController chatController;


    // 好友聊天窗口映射表
    private Map<String, Stage> friendStages = new HashMap<>();


    public static ObservableList<String> names =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        chatController = this;
        friendsListView.setItems(names);
        friendsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    // 获取选中的item
                    String selectedItem = friendsListView.getSelectionModel().getSelectedItem();
                    System.out.println("Selected item: " + selectedItem);
                    getOrCreateFriendStage(selectedItem);
                }
            }
        });
    }

    public void joinGroup(){
        group.add(names.get(0));
        Client client=Client.getClient();
        client.joinGroup=true;
        Platform.runLater(() -> {
            try {
                // 加载FXML文件
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("groupChat.fxml"));
                Parent root = fxmlLoader.load();
                // 创建Scene和Stage
                Scene scene = new Scene(root, 600, 400);
                Stage groupStage = new Stage();
                groupStage.setTitle("Group Chat");
                groupStage.setScene(scene);
                //通知群聊其他人
                ChatMessage newMessage = new ChatMessage(client.user,"群发","加入聊天",null);
                Gson gson1 = new Gson();
                String newJsonMessage = gson1.toJson(newMessage);
                client.send(newJsonMessage);
                // 设置关闭事件处理器
                groupStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        // 在窗口关闭时执行的操作，例如关闭连接等
                        // ...
                        ChatMessage chatMessage = new ChatMessage(client.user, "群发", "退出聊天", null);
                        Gson gson = new Gson();
                        String jsonMessage = gson.toJson(chatMessage);
                        client.send(jsonMessage);
                    }
                });

                // 显示窗口
                groupStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public boolean getFriendStage(String friend){
        if (friendStages.containsKey(friend)) {
            return true;
        }
        return false;
    }



    public Stage getOrCreateFriendStage(String friend) {
        if (friendStages.containsKey(friend)) {
            return friendStages.get(friend);
        } else {
            // 创建新的好友聊天窗口
            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FriendChat.fxml"));
                Parent root;
                try {
                    root = fxmlLoader.load();
                    friendChatController.setFriendName(friend);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage friendStage = new Stage();
                friendStage.setScene(new Scene(root));
                friendStage.setTitle(friend + "的聊天窗口");
                friendStage.show();
                friendStages.put(friend, friendStage);
                friendStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                       //退出聊天时，给对方发送信息
                        //client是我，friend是对方
                        Client client=Client.getClient();
                        ChatMessage chatMessage = new ChatMessage(client.user,friend,"我已退出聊天，请勿再给我发送信息",null);
                        Gson gson = new Gson();
                        String jsonMessage = gson.toJson(chatMessage);
                        client.send(jsonMessage);
                        friendStages.remove(friend);
                    }
                });
            });
            return null;
        }

    }
}

