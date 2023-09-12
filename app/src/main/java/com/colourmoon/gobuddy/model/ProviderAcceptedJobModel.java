package com.colourmoon.gobuddy.model;

public class ProviderAcceptedJobModel {
    private String id;
    private String orderId;
    private String serviceDate;
    private String serviceTime;
    private String serviceTitle;
    private String subServiceTitle;
    private String providerResponsibility;
    private String customerResponsibility;
    private String note;
    private String location;
    private String locality;
    private String gender;
    private String paymentMode;
    private String name;
    private String latitude;
    private String longitude;
    private String phoneNumber;
    private String dateAndTime;
    private String orderStatus;
    private String isJobCompletedStatus;
    private String totalAmount;

    public ProviderAcceptedJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                    String subServiceTitle, String providerResponsibility, String customerResponsibility,
                                    String note, String location, String locality, String gender, String name, String latitude,
                                    String longitude, String phoneNumber, String dateAndTime, String orderStatus,
                                    String isJobCompletedStatus, String paymentMode, String totalAmount) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.providerResponsibility = providerResponsibility;
        this.customerResponsibility = customerResponsibility;
        this.note = note;
        this.location = location;
        this.locality = locality;
        this.gender = gender;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.dateAndTime = dateAndTime;
        this.orderStatus = orderStatus;
        this.isJobCompletedStatus = isJobCompletedStatus;
        this.paymentMode = paymentMode;
        this.totalAmount = totalAmount;
    }

    public ProviderAcceptedJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                    String subServiceTitle, String providerResponsibility, String customerResponsibility,
                                    String note, String location, String locality, String gender, String name, String latitude,
                                    String longitude, String phoneNumber, String dateAndTime, String totalAmount, String paymentMode) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.providerResponsibility = providerResponsibility;
        this.customerResponsibility = customerResponsibility;
        this.note = note;
        this.location = location;
        this.locality = locality;
        this.gender = gender;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.dateAndTime = dateAndTime;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
    }

    public ProviderAcceptedJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                    String subServiceTitle, String location, String locality, String dateAndTime,
                                    String totalAmount, String paymentMode) {
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

    public String getProviderResponsibility() {
        return providerResponsibility;
    }

    public String getCustomerResponsibility() {
        return customerResponsibility;
    }

    public String getNote() {
        return note;
    }

    public String getLocation() {
        return location;
    }

    public String getLocality() {
        return locality;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getIsJobCompletedStatus() {
        return isJobCompletedStatus;
    }
}
