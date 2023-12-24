package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryModel implements Parcelable {

//    @SerializedName("sid")
    @SerializedName("id")
    private String subCategoryId;

    @SerializedName("sub_category")
    private String subCategoryName;

    @SerializedName("sub_image")
    private String subImage;

    private List<ServiceModel> services;

    @SerializedName("isChecked")
    private boolean isCategoryChecked;

    private int type;
    private boolean isShow;

    private int headerIndex;

    protected SubCategoryModel(Parcel in) {
        subCategoryId = in.readString();
        subCategoryName = in.readString();

        services = in.createTypedArrayList(ServiceModel.CREATOR);
        isCategoryChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subCategoryId);
        dest.writeString(subCategoryName);
        dest.writeTypedList(services);
        dest.writeByte((byte) (isCategoryChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryModel> CREATOR = new Creator<SubCategoryModel>() {
        @Override
        public SubCategoryModel createFromParcel(Parcel in) {
            return new SubCategoryModel(in);
        }

        @Override
        public SubCategoryModel[] newArray(int size) {
            return new SubCategoryModel[size];
        }
    };

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public boolean getIsCategoryChecked() {
        return isCategoryChecked;
    }

    public void setIsCategoryChecked(boolean isCategoryChecked) {
        this.isCategoryChecked = isCategoryChecked;
    }

    public SubCategoryModel() {

    }

    public SubCategoryModel(String subCategoryId, String subCategoryName,String subImage, boolean isCategoryChecked) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.subImage = subImage;
        this.isCategoryChecked = isCategoryChecked;

    }

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getHeaderIndex() {
        return headerIndex;
    }

    public void setHeaderIndex(int headerIndex) {
        this.headerIndex = headerIndex;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }
}
