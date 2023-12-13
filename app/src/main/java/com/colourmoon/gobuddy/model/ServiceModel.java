package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ServiceModel implements Parcelable {

    @SerializedName("sid")
    private String serviceId;
    @SerializedName("title")
    private String serviceTitle;
    @SerializedName("price")
    private String servicePrice;
    @SerializedName("presponsibility")
    private String serviceProviderResponsibility;
    @SerializedName("cresponsibility")
    private String serviceCustomerResponsibility;
    @SerializedName("note")
    private String serviceNote;

    @SerializedName("sub_services")
    private String subServiceId;


    private String sub_image;

    private String subCategoryId;

    public ServiceModel() {

    }

    public ServiceModel(String serviceId, String serviceTitle, String servicePrice, String serviceProviderResponsibility, String serviceCustomerResponsibility, String serviceNote, String subServiceId) {
        this.serviceId = serviceId;
        this.serviceTitle = serviceTitle;
        this.servicePrice = servicePrice;
        this.serviceProviderResponsibility = serviceProviderResponsibility;
        this.serviceCustomerResponsibility = serviceCustomerResponsibility;
        this.serviceNote = serviceNote;
        this.subServiceId = subServiceId;
    }

    protected ServiceModel(Parcel in) {
        serviceId = in.readString();
        serviceTitle = in.readString();
        servicePrice = in.readString();
        serviceProviderResponsibility = in.readString();
        serviceCustomerResponsibility = in.readString();
        serviceNote = in.readString();
        subServiceId = in.readString();
    }

    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
    }

    public static final Creator<ServiceModel> CREATOR = new Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel in) {
            return new ServiceModel(in);
        }

        @Override
        public ServiceModel[] newArray(int size) {
            return new ServiceModel[size];
        }
    };

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getServiceProviderResponsibility() {
        return serviceProviderResponsibility;
    }

    public String getServiceCustomerResponsibility() {
        return serviceCustomerResponsibility;
    }

    public String getServiceNote() {
        return serviceNote;
    }

    public String getSubServiceId() {
        if (subServiceId == null) {
            return "0";
        }
        return subServiceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serviceId);
        parcel.writeString(serviceTitle);
        parcel.writeString(servicePrice);
        parcel.writeString(serviceProviderResponsibility);
        parcel.writeString(serviceCustomerResponsibility);
        parcel.writeString(serviceNote);
        parcel.writeString(subServiceId);
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSub_image() {
        return sub_image;
    }

    public void setSub_image(String sub_image) {
        this.sub_image = sub_image;
    }
}
