package com.colourmoon.gobuddy.model;

import com.google.gson.annotations.SerializedName;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;

public class CategoryModel extends MultiCheckExpandableGroup {

    @SerializedName("id")
    private String categoryId;

    @SerializedName("category")
    private String categoryName;

    public CategoryModel(String categoryId, String categoryName, List<SubCategoryModel> subCategoryModelList) {
        super(categoryName, subCategoryModelList);
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
