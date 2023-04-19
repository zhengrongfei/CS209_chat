package com.example.Controller;

import com.example.Dao.UserDao;
import com.example.Json.ChatMessage;
import com.example.client.Client;
import com.example.demo.HelloApplication;
import com.example.entity.User;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import static com.example.Controller.ChatController.names;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button loginButton;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 在这里进行登录验证，可以调用数据库服务或其他业务逻辑
        try {
            User user = UserDao.getUserByUsername(username);
            names.add(username);
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        // 在这里执行与服务器连接相关的操作
                        Client.startClient(username);

                        return null;
                    }
                };
// 打开FXML窗口
                Platform.runLater(() -> {
                    try {
                        // 加载FXML文件
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat.fxml"));
                        Parent root = fxmlLoader.load();
                        // 创建Scene和Stage
                        Scene scene = new Scene(root, 600, 400);
                        Stage chatStage = new Stage();
                        chatStage.setTitle("Fei Chat");
                        chatStage.setScene(scene);
                        // 设置关闭事件处理器
                        chatStage.setOnCloseRequest(event -> {
                            // 在窗口关闭时执行的操作，例如关闭连接等
                            // ...
                        });
                        // 显示窗口
                        chatStage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


// 启动任务
                new Thread(task).start();




            } else {
                showAlert(AlertType.ERROR, "登录失败", "用户名或密码错误，请重试。");
            }
        }catch (NullPointerException e){
            showAlert(AlertType.ERROR, "登录失败","用户 "+ username+" 不存在,请重新输入!");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
