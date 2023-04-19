package com.example.Json;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ChatMessage {
    private String sender; // 发送方
    private String receiver; // 接收方
    private String content; // 消息内容
    public ObservableList<String> onlineUser;

    public ChatMessage(String sender, String receiver, String content ,ObservableList<String>  onlineUser) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.onlineUser=onlineUser;
    }

    // Getter 和 Setter 方法

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public  ObservableList<String>  getOnlineUser() {
        return onlineUser;
    }

    public  void setOnlineUser(ObservableList<String>  onlineUser) {
        this.onlineUser = onlineUser;
    }
}
