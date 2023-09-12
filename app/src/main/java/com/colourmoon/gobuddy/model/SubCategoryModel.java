package com.colourmoon.gobuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubCategoryModel implements Parcelable {

    @SerializedName("sid")
    private String subCategoryId;

    @SerializedName("sub_category")
    private String subCategoryName;

    @SerializedName("isChecked")
    private boolean isCategoryChecked;

    protected SubCategoryModel(Parcel in) {
        subCategoryId = in.readString();
        subCategoryName = in.readString();
        isCategoryChecked = in.readByte() != 0;
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

    public SubCategoryModel(String subCategoryId, String subCategoryName, boolean isCategoryChecked) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.isCategoryChecked = isCategoryChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subCategoryId);
        parcel.writeString(subCategoryName);
        parcel.writeByte((byte) (isCategoryChecked ? 1 : 0));
    }
}
