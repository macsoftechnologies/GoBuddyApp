package com.colourmoon.gobuddy.model;

public class FavouriteModel {
    private String jobId;
    private String orderId;
    private String jobServiceName;
    private String jobSubServiceName;
    private String jobDate;
    private String jobPrice;
    private String serviceId;
    private String subServiceId;
    private String subCategoryId;

    public FavouriteModel(String jobId, String orderId, String jobServiceName, String jobSubServiceName, String jobDate, String jobPrice,
                          String serviceId, String subServiceId, String subCategoryId) {
        this.jobId = jobId;
        this.orderId = orderId;
        this.jobServiceName = jobServiceName;
        this.jobSubServiceName = jobSubServiceName;
        this.jobDate = jobDate;
        this.jobPrice = jobPrice;
        this.serviceId = serviceId;
        this.subServiceId = subServiceId;
        this.subCategoryId = subCategoryId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getJobServiceName() {
        return jobServiceName;
    }

    public String getJobSubServiceName() {
        return jobSubServiceName;
    }

    public String getJobDate() {
        return jobDate;
    }

    public String getJobPrice() {
        return jobPrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }
}
