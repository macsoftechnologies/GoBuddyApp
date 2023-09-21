package com.colourmoon.gobuddy.model;

import java.util.UUID;

public class PaytmModel {

    private String mId;

    private String orderId;

    private String custId;

    private String channelId;

    private String txnAmount;

    private String website;

    private String callBackUrl;

    private String industryTypeId;

    public PaytmModel(String mId, String orderId, String custId, String channelId, String txnAmount, String website, String callBackUrl, String industryTypeId) {
        this.mId = mId;
        this.orderId = generateUniqueOrderId();
        this.custId = custId;
        this.channelId = channelId;
        this.txnAmount = txnAmount;
        this.website = website;
        this.callBackUrl = callBackUrl;
        this.industryTypeId = industryTypeId;
    }

    public String getmId() {
        return mId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustId() {
        return custId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public String getWebsite() {
        return website;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public String generateUniqueOrderId() {
        return UUID.randomUUID().toString();
    }
}
