package com.colourmoon.gobuddy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationbasedSubCategoriesModel implements Parcelable {


        //    @SerializedName("sid")
        @SerializedName("id")
        private String subCategoryId;

        @SerializedName("sub_category")
        private String subCategoryName;

//       @SerializedName("location_price")
//       private String loctionprice;

        @SerializedName("sub_image")
        private String subImage;





        private List<LocationServiceModel> services;

        @SerializedName("isChecked")
        private boolean isCategoryChecked;

        private int type;
        private boolean isShow;

        private int headerIndex;


    protected LocationbasedSubCategoriesModel(Parcel in) {
            subCategoryId = in.readString();
            subCategoryName = in.readString();
           // loctionprice = in.readString();
            services = in.createTypedArrayList(LocationServiceModel.CREATOR);
            isCategoryChecked = in.readByte() != 0;
        }

    public LocationbasedSubCategoriesModel(String subCategoryId, String subCategoryName, String subImage, boolean isCategoryChecked) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
//        this.loctionprice = location_price;
        this.subImage = subImage;
        this.isCategoryChecked = isCategoryChecked;


    }

//        public LocationbasedSubCategoriesModel(String sid, String sub_category, String location_price, String sub_image, boolean b) {
//            this.subCategoryId = sid;
//            this.subCategoryName = sub_category;
//            this.isCategoryChecked = b;
//            this.loctionprice = location_price;
//
//        }

//        public LocationbasedSubCategoriesModel() {
//
//        }

//    public LocationbasedSubCategoriesModel(String id, String sub_category, String location_price, String sub_image, boolean b) {
//    }

    public LocationbasedSubCategoriesModel() {

    }






    @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(subCategoryId);

            dest.writeString(subCategoryName);
           // dest.writeString(loctionprice);
            dest.writeTypedList(services);

            dest.writeByte((byte) (isCategoryChecked ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<LocationbasedSubCategoriesModel> CREATOR = new Creator<LocationbasedSubCategoriesModel>() {
            @Override
            public LocationbasedSubCategoriesModel createFromParcel(Parcel in) {
                return new LocationbasedSubCategoriesModel(in);
            }

            @Override
            public LocationbasedSubCategoriesModel[] newArray(int size) {
                return new LocationbasedSubCategoriesModel[size];
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



//        public LocationbasedSubCategoriesModel(String subCategoryId, String subCategoryName,String subImage, boolean isCategoryChecked) {
//            this.subCategoryId = subCategoryId;
//
//            this.subCategoryName = subCategoryName;
//            this.subImage = subImage;
//            this.isCategoryChecked = isCategoryChecked;
//
//        }
//public LocationbasedSubCategoriesModel(String subCategoryId, String subCategoryName, String subImage, boolean isCategoryChecked) {
//    this.subCategoryId = subCategoryId;
//
//            this.subCategoryName = subCategoryName;
//            this.subImage = subImage;
//            this.isCategoryChecked = isCategoryChecked;
//
//}

        public List<LocationServiceModel> getServices() {
            return services;
        }

        public void setServices(List<LocationServiceModel> services) {
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

//       public String getLoctionprice() {
//        return loctionprice;
//    }
//
//      public void setLoctionprice(String loctionprice) {
//        this.loctionprice = loctionprice;
//    }

}

