package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class ProductRattingsItem {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("avg_ratting")
    private String avgRatting;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAvgRatting() {
        return avgRatting;
    }

    public void setAvgRatting(String avgRatting) {
        this.avgRatting = avgRatting;
    }

    // Getters and Setters for all fields
}
