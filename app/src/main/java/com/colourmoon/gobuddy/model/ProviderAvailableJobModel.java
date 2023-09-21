package com.colourmoon.gobuddy.model;

public class ProviderAvailableJobModel {

    private String id;
    private String orderId;
    private String serviceDate;
    private String serviceTime;
    private String serviceTitle;
    private String subServiceTitle;
    private String providerResponsibility;
    private String customerResponsibility;
    private String note;
    private String houseStreet;
    private String dateAndTime;
    private String locality;
    private String customerName;
    private String gender;
    private String paymentMode;
    private String jobPrice;

    public ProviderAvailableJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                     String subServiceTitle, String providerResponsibility, String customerResponsibility,
                                     String note, String houseStreet, String dateAndTime, String locality, String customerName,
                                     String gender) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.providerResponsibility = providerResponsibility;
        this.customerResponsibility = customerResponsibility;
        this.note = note;
        this.houseStreet = houseStreet;
        this.dateAndTime = dateAndTime;
        this.locality = locality;
        this.customerName = customerName;
        this.gender = gender;
    }

    public ProviderAvailableJobModel(String id, String orderId, String serviceDate, String serviceTime, String serviceTitle,
                                     String subServiceTitle, String dateAndTime, String paymentMode, String jobPrice,
                                     String locality) {
        this.id = id;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.dateAndTime = dateAndTime;
        this.paymentMode = paymentMode;
        this.jobPrice = jobPrice;
        this.locality = locality;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getJobPrice() {
        return jobPrice;
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

    public String getHouseStreet() {
        return houseStreet;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getLocality() {
        return locality;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getGender() {
        return gender;
    }
}
