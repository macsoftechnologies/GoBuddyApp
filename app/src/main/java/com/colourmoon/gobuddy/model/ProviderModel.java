package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProviderModel implements Parcelable {

    private String providerId;
    private String providerName;
    private String providerProfileImage;
    private String providerToken;
    private String providerDistance;
    private String providerEmail;
    private String providerPhoneNumber;
    private String providerRating;

    public ProviderModel(String providerId, String providerName, String providerProfileImage, String providerToken, String providerDistance,
                         String providerEmail, String providerPhoneNumber, String providerRating) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.providerProfileImage = providerProfileImage;
        this.providerToken = providerToken;
        this.providerDistance = providerDistance;
        this.providerEmail = providerEmail;
        this.providerPhoneNumber = providerPhoneNumber;
        this.providerRating = providerRating;
    }

    protected ProviderModel(Parcel in) {
        providerId = in.readString();
        providerName = in.readString();
        providerProfileImage = in.readString();
        providerToken = in.readString();
        providerDistance = in.readString();
        providerEmail = in.readString();
        providerPhoneNumber = in.readString();
        providerRating = in.readString();
    }

    public static final Creator<ProviderModel> CREATOR = new Creator<ProviderModel>() {
        @Override
        public ProviderModel createFromParcel(Parcel in) {
            return new ProviderModel(in);
        }

        @Override
        public ProviderModel[] newArray(int size) {
            return new ProviderModel[size];
        }
    };

    public String getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderProfileImage() {
        return providerProfileImage;
    }

    public String getProviderToken() {
        return providerToken;
    }

    public String getProviderDistance() {
        return providerDistance;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public String getProviderPhoneNumber() {
        return providerPhoneNumber;
    }

    public String getProviderRating() {
        return providerRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerId);
        dest.writeString(providerName);
        dest.writeString(providerProfileImage);
        dest.writeString(providerToken);
        dest.writeString(providerDistance);
        dest.writeString(providerEmail);
        dest.writeString(providerPhoneNumber);
        dest.writeString(providerRating);
    }
}
