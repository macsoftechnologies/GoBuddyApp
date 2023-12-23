package com.colourmoon.gobuddy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyOtpResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("provider")
    private String provider;
    @SerializedName("otp_verified")
    private String otp_verified;
    @SerializedName("user_status")
    private String user_status;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("view_as")
    private String view_as;
    @SerializedName("ekyc")
    private String ekyc;

    // Getter and setter methods for each field
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOtp_verified() {
        return otp_verified;
    }

    public void setOtp_verified(String otp_verified) {
        this.otp_verified = otp_verified;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getView_as() {
        return view_as;
    }

    public void setView_as(String view_as) {
        this.view_as = view_as;
    }

    public String getEkyc() {
        return ekyc;
    }

    public void setEkyc(String ekyc) {
        this.ekyc = ekyc;
    }


}

