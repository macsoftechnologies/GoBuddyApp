package com.colourmoon.gobuddy.model;

import com.google.gson.annotations.SerializedName;

public class OtpVerificationResponseModel {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("customer")
    private String isCustomer;

    @SerializedName("provider")
    private String isProvider;

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

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getIsProvider() {
        return isProvider;
    }

    public void setIsProvider(String isProvider) {
        this.isProvider = isProvider;
    }
}
