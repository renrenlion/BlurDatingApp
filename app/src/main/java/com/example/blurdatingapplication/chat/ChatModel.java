package com.example.blurdatingapplication.chat;

import com.google.firebase.Timestamp;

public class ChatModel {

    private String message;
    private String senderId;
    private Timestamp timestamp;

    private String unblurRequestSenderId;  // ID of the user who sent the unblur request
    private boolean unblurRequestAccepted;

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

    public String getUnblurRequestSenderId() {
        return unblurRequestSenderId;
    }

    public void setUnblurRequestSenderId(String unblurRequestSenderId) {
        this.unblurRequestSenderId = unblurRequestSenderId;
    }

    public boolean isUnblurRequestAccepted() {
        return unblurRequestAccepted;
    }

    public void setUnblurRequestAccepted(boolean unblurRequestAccepted) {
        this.unblurRequestAccepted = unblurRequestAccepted;
    }
}



