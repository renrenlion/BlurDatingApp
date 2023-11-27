package com.example.blurdatingapplication.chat;

import com.google.firebase.Timestamp;

public class ChatModel {

    private String message;
    private String senderId;
    private Timestamp timestamp;

    public ChatModel() {

    }

    public ChatModel(Timestamp timestamp, String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}



