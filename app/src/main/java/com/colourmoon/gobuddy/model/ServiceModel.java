package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceModel implements Parcelable {

    private String serviceId;
    private String serviceTitle;
    private String servicePrice;
    private String serviceProviderResponsibility;
    private String serviceCustomerResponsibility;
    private String serviceNote;
    private String subServiceId;

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
}
