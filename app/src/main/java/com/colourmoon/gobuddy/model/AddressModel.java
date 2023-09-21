package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Parcelable {
    private String addressId;
    private String gender;
    private String name;
    private String house_street;
    private String locality;
    private String nickName;
    private String pincode;

    public AddressModel(String addressId, String gender, String name, String house_street, String locality) {
        this.addressId = addressId;
        this.gender = gender;
        this.name = name;
        this.house_street = house_street;
        this.locality = locality;
    }

    public AddressModel(String addressId, String gender, String name, String house_street, String locality, String nickName,String pincode) {
        this.addressId = addressId;
        this.gender = gender;
        this.name = name;
        this.house_street = house_street;
        this.locality = locality;
        this.nickName = nickName;
        this.pincode = pincode;
    }

    protected AddressModel(Parcel in) {
        addressId = in.readString();
        gender = in.readString();
        name = in.readString();
        house_street = in.readString();
        locality = in.readString();
        nickName = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getAddressId() {
        return addressId;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getHouse_street() {
        return house_street;
    }

    public String getLocality() {
        return locality;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPincode() {
        return pincode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressId);
        dest.writeString(gender);
        dest.writeString(name);
        dest.writeString(house_street);
        dest.writeString(locality);
        dest.writeString(nickName);
        dest.writeString(pincode);
    }
}
