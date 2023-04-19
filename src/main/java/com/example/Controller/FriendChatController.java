package com.example.Controller;


import com.example.Json.ChatMessage;
import com.example.client.Client;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.Controller.ChatController.chatController;

public class FriendChatController {
    @FXML
    private ListView<String> friendsListView;

    @FXML
    private TextArea chatTextArea;

    public static FriendChatController friendChatController;
    @FXML
    private TextField messageTextField;

    private String friendName;

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @FXML
    public void initialize() {
        friendChatController = this;
        chatTextArea.setEditable(false);
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void sendMessage() {
        String message = messageTextField.getText().trim();
        if(message.equals("")){
            showAlert(Alert.AlertType.ERROR, "发送失败","不能发送空消息");
        }
        //TODO:发送webSocket消息
        Client client=Client.getClient();
        ChatMessage chatMessage = new ChatMessage(client.user,friendName,message ,null);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(chatMessage);
        client.send(jsonMessage);
        Platform.runLater(() -> {
                    Stage friendStage = chatController.getOrCreateFriendStage(friendName);
                    // 在好友聊天窗口中显示消息
                    TextArea friendChatTextArea = (TextArea) friendStage.getScene().lookup("#chatTextArea");
                    friendChatTextArea.appendText("[我]: " + message.replace("\\n","\n") + "\n");
                    // 清空输入框
                    messageTextField.clear();
                });
        }


}
