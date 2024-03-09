package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class VendorsDetailsRattingsItem {

    @SerializedName("vendor_id")
    private String vendorId;

    @SerializedName("avg_ratting")
    private String avgRatting;

    // Constructor, Getters, and Setters


    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getAvgRatting() {
        return avgRatting;
    }

    public void setAvgRatting(String avgRatting) {
        this.avgRatting = avgRatting;
    }
}
