package com.example.Controller;

import com.example.Json.ChatMessage;
import com.example.client.Client;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.Controller.ChatController.chatController;

public class GroupChatController {
    public static GroupChatController groupChatController;

    //维护一个在线的人的list
    public static ObservableList<String> group =
            FXCollections.observableArrayList();

    @FXML
    public ListView<String> groupListView;
    @FXML
    public TextArea groupChatTextArea;

    @FXML
    private TextField groupMessageTextField;

    @FXML
    public void initialize() {
        groupChatController = this;
        groupChatTextArea.setEditable(false);
        groupListView.setItems(group);
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
        String message = groupMessageTextField.getText().trim();
        if(message.equals("")){
            showAlert(Alert.AlertType.ERROR, "发送失败","不能发送空消息");
        }
        //TODO:发送webSocket消息
        Client client=Client.getClient();
        ChatMessage chatMessage = new ChatMessage(client.user,"群发",message ,null);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(chatMessage);
        client.send(jsonMessage);
        Platform.runLater(() -> {
            // 清空输入框
            groupMessageTextField.clear();
        });
    }
}
