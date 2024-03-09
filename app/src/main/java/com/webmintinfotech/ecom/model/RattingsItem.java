package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class RattingsItem {

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("avg_ratting")
    private String avgRatting;

    // Constructor, getters, and setters


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getAvgRatting() {
        return avgRatting;
    }

    public void setAvgRatting(String avgRatting) {
        this.avgRatting = avgRatting;
    }
}
