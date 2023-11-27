package com.example.blurdatingapplication.chat;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ChatroomModel {

    private Timestamp mostRecentTimeStamp;
    private String lastMSenderId;
    private String lastM;
    private List<String> userIds;
    private String chatroomId;

    public ChatroomModel() {
        // Default constructor required for Firestore
    }

    public ChatroomModel(Timestamp mostRecentTimeStamp, String lastMSenderId, String lastM, List<String> userIds, String chatroomId) {
        this.mostRecentTimeStamp = mostRecentTimeStamp;
        this.lastMSenderId = lastMSenderId;
        this.lastM = lastM;
        this.userIds = userIds;
        this.chatroomId = chatroomId;

        // null checks
        if (this.userIds == null) {
            this.userIds = new ArrayList<>();
        }
    }

    public <T> ChatroomModel(String currentUserId, String s, List<T> asList, String chatroomId) {
    }

    public Timestamp getMostRecentTimeStamp() {
        return mostRecentTimeStamp;
    }

    public void setMostRecentTimeStamp(Timestamp mostRecentTimeStamp) {
        this.mostRecentTimeStamp = mostRecentTimeStamp;
    }

    public String getLastMSenderId() {
        return lastMSenderId;
    }

    public void setLastMSenderId(String lastMSenderId) {
        this.lastMSenderId = lastMSenderId;
    }

    public String getLastM() {
        return lastM;
    }

    public void setLastM(String lastM) {
        this.lastM = lastM;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }
}
