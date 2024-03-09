package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class ProductProductimage {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("id")
    private Integer id;

    // Getters and Setters for all fields


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
