package com.colourmoon.gobuddy.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("provider")
    private String isProvider;

    @SerializedName("otp_verified")
    private String isOtpVerified;

    @SerializedName("user_status")
    private String userStatus;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("view_as")
    private String presentUserType;

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

    public String getIsProvider() {
        return isProvider;
    }

    public void setIsProvider(String isProvider) {
        this.isProvider = isProvider;
    }

    public String getIsOtpVerified() {
        return isOtpVerified;
    }

    public void setIsOtpVerified(String isOtpVerified) {
        this.isOtpVerified = isOtpVerified;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPresentUserType() {
        return presentUserType;
    }

    public void setPresentUserType(String presentUserType) {
        this.presentUserType = presentUserType;
    }
}
