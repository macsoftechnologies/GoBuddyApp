package com.colourmoon.gobuddy.model;

public class ProfileModel {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String imageUrl;
    private String dob;

    public ProfileModel(String name, String email, String phoneNumber, String address, String imageUrl, String dob) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDob() {
        return dob;
    }
}
