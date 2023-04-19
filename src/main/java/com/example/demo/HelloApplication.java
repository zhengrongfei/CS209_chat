package com.example.demo;

import com.example.Dao.UserDao;
import com.example.Json.ChatMessage;
import com.example.client.Client;
import com.example.entity.User;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://rm-bp1f1g5s9130l993jwo.mysql.rds.aliyuncs.com:3306/chat";
        String username = "zhengrongfei";
        String password = "Qa123456";

        // 创建MySQLConnector实例
        MySQLConnector connector = new MySQLConnector(url, username, password);

        try {
            // 连接数据库
            connector.connect();
            // 执行查询操作
            User user=UserDao.getUserByUsername("zhengrogfei");
            System.out.println(user.getPassword());

        } catch (SQLException e) {
            // 处理SQLException异常，输出错误信息
            System.err.println("数据库操作异常：" + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("用户不存在");
        }

        launch();

    }
}