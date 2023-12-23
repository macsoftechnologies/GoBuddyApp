package com.colourmoon.gobuddy.model;

public class SearchModel {
    private String serviceId;
    private String subServiceId;
    private String serviceTitle;
    private String servicePrice;
    private String serviceSubCategoryId;
    private String serviceCustomerResponsibility;
    private String serviceProviderResponsibility;
    private String serviceNoteResponsibility;
    private String sub_image;

    public SearchModel(String serviceId, String subServiceId, String serviceTitle, String servicePrice,
                       String serviceSubCategoryId, String serviceCustomerResponsibility, String serviceProviderResponsibility, String serviceNoteResponsibility) {
        this.serviceId = serviceId;
        this.subServiceId = subServiceId;
        this.serviceTitle = serviceTitle;
        this.servicePrice = servicePrice;
        this.serviceSubCategoryId = serviceSubCategoryId;
        this.serviceCustomerResponsibility = serviceCustomerResponsibility;
        this.serviceProviderResponsibility = serviceProviderResponsibility;
        this.serviceNoteResponsibility = serviceNoteResponsibility;
        this.sub_image = sub_image;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getServiceSubCategoryId() {
        return serviceSubCategoryId;
    }

    public String getServiceCustomerResponsibility() {
        return serviceCustomerResponsibility;
    }

    public String getServiceProviderResponsibility() {
        return serviceProviderResponsibility;
    }

    public String getServiceNoteResponsibility() {
        return serviceNoteResponsibility;
    }


    public String getSub_image() {
        return sub_image;
    }
}
