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

    @SerializedName("count")
    private String serviceCount;

    public ServiceCategoryModel(String serviceId, String serviceName, String serviceImageUrl,String serviceCount) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceImageUrl = serviceImageUrl;
        this.serviceCount= serviceCount;
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

    public String getServiceCount() {
        return serviceCount;
    }
}
