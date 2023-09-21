package com.colourmoon.gobuddy.model;

public class OrderDetailsModel {
    private String serviceDate;
    private String serviceTime;
    private String servicePrice;
    private String serviceTitle;
    private String extra_charges_title;
    private String extra_charges_price;
    private String total;
    private String sub_category;
    private String category_id;

    public OrderDetailsModel(String serviceDate, String serviceTime, String servicePrice, String serviceTitle,String extra_charges_title
            ,String extra_charges_price,String total,String sub_category,String category_id) {
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.servicePrice = servicePrice;
        this.serviceTitle = serviceTitle;
        this.extra_charges_title = extra_charges_title;
        this.extra_charges_price = extra_charges_price;
        this.total = total;
        this.sub_category = sub_category;
        this.category_id = category_id;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public String getExtra_charges_price() {
        return extra_charges_price;
    }

    public String getExtra_charges_title() {
        return extra_charges_title;
    }

    public String getTotal() {
        return total;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSub_category() {
        return sub_category;
    }
}
