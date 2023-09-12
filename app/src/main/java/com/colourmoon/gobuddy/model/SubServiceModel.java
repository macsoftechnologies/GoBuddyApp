package com.colourmoon.gobuddy.model;

public class SubServiceModel {
    private String subServiceId;
    private String subServiceTitle;
    private String subServicePrice;

    public SubServiceModel(String subServiceId, String subServiceTitle, String subServicePrice) {
        this.subServiceId = subServiceId;
        this.subServiceTitle = subServiceTitle;
        this.subServicePrice = subServicePrice;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public String getSubServiceTitle() {
        return subServiceTitle;
    }

    public String getSubServicePrice() {
        return subServicePrice;
    }
}
