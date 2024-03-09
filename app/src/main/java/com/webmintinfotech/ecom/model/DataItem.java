package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class DataItem {
    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("id")
    private Integer id;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
