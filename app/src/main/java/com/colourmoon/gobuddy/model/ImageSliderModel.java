package com.colourmoon.gobuddy.model;

public class ImageSliderModel {

    private String image_url;
    private String category_id;

    public ImageSliderModel(String image_url, String category_id) {
        this.image_url = image_url;
        this.category_id = category_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getCategory_id() {
        return category_id;
    }
}
