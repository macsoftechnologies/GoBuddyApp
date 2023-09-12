package com.colourmoon.gobuddy.model;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponseModel {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("view_as")
    private String presentUserType;

    public String getPresentUserType() {
        return presentUserType;
    }

    public void setPresentUserType(String presentUserType) {
        this.presentUserType = presentUserType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
