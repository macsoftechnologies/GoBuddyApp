package com.colourmoon.gobuddy.model;

public class ProviderCompletedJobModel {
    private String id;
    private String orderId;
    private String serviceDate;
    private String serviceTime;
    private String serviceTitle;
    private String subServiceTitle;
    private String subTotal;
    private String extraAmount;
    private String orderStatus;
    private String totalAmount;

    public ProviderCompletedJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle, String subServiceTitle,
                                     String subTotal, String extraAmount, String orderStatus, String totalAmount) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.subTotal = subTotal;
        this.extraAmount = extraAmount;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public String getSubServiceTitle() {
        return subServiceTitle;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
