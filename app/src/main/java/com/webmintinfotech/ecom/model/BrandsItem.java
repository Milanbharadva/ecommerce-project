package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class BrandsItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("id")
    private Integer id;

    // Constructor, getters, and setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
