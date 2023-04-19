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


import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.Controller.FriendChatController.friendChatController;
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
            });
            return null;
        }

    }
}

