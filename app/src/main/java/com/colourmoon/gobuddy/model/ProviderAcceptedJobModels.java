package com.colourmoon.gobuddy.model;

public class ProviderAcceptedJobModels {


    private String id;
    private String orderId;
    private String serviceDate;
    private String serviceTime;
    private String serviceTitle;
    private String subServiceTitle;

    private String location;
    private String locality;
    private String paymentMode;



    private String remainingamount;
    private String paidamount;

    private String dateAndTime;

    private String totalAmount;

    public ProviderAcceptedJobModels(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                    String subServiceTitle, String location, String locality, String dateAndTime,
                                    String totalAmount,String remainingamount,String paidamount,String paymentMode) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.location = location;
        this.locality = locality;
        this.dateAndTime = dateAndTime;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
        this.paidamount = paidamount;
        this.remainingamount = remainingamount;
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

    public String getLocation() {
        return location;
    }

    public String getLocality() {
        return locality;
    }

    public String getPaymentMode() {
        return paymentMode;
    }


    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getRemainingamount() {
        return remainingamount;
    }

    public String getPaidamount() {
        return paidamount;
    }

}
