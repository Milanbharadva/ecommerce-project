package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class WishListRattingsItem {

    @SerializedName("product_id")
    private Integer productId;

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

    @SerializedName("avg_ratting")
    private String avgRatting;

    // Getters and setters
}
