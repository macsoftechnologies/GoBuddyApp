package com.colourmoon.gobuddy;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class LocationSubserviceModel {


    private String subServiceId;
    private String subServiceTitle;
    private String subServicePrice;
    private String locationPrice;



//    public LocationSubserviceModel(String subServiceId, String subServiceTitle, String subServicePrice, String locationPrice) {
//        this.subServiceId = subServiceId;
//        this.subServiceTitle = subServiceTitle;
//        this.subServicePrice = subServicePrice;
//        this.locationPrice = locationPrice;
//
//    }



    public LocationSubserviceModel(String subServiceId, String subServiceTitle, String subServicePrice) {
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

    public String getLocationPrice() {
        return locationPrice;
    }
}
