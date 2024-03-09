package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class ViewAllProductimage {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("id")
    private Integer id;

    // Constructor, Getters, and Setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
