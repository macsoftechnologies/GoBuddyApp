package com.colourmoon.gobuddy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceCategoryModel implements Serializable {

    @SerializedName("id")
    private String serviceId;

    @SerializedName("category")
    private String serviceName;

    @SerializedName("image")
    private String serviceImageUrl;

    public ServiceCategoryModel(String serviceId, String serviceName, String serviceImageUrl) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceImageUrl = serviceImageUrl;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }
}
