package com.colourmoon.gobuddy;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderImagesResponse {
    private String image;
    private String category_id;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("sliders")
    private List<Slider> sliders;


    public SliderImagesResponse(String image, String category_id) {
        this.image = image;
        this.category_id = category_id;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    // Getter and setter methods for each field
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    public byte[] bytes() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json.getBytes();
    }


    // Inner class representing the structure of each slider object
    public static class Slider {
        @SerializedName("id")
        private String id;

        @SerializedName("image")
        private String image;

        @SerializedName("category_id")
        private String categoryId;

        @SerializedName("status")
        private String status;

        @SerializedName("created_date")
        private String createdDate;

        @SerializedName("updated_date")
        private String updatedDate;

        @SerializedName("location_id")
        private String locationId;

        // Getter and setter methods for each field
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }
    }
}
