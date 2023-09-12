package com.colourmoon.gobuddy.model;

public class ChatModel {

    private String message;
    private String userType;
    private String messageTime;
    private String userId;

    public ChatModel(String message, String userType, String messageTime, String userId) {
        this.message = message;
        this.userType = userType;
        this.messageTime = messageTime;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getUserType() {
        return userType;
    }

    public String getMessageTime() {
        return messageTime;
    }
}
