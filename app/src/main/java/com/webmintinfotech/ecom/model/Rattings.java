package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class Rattings {
    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Double getAvgRatting() {
        return avgRatting;
    }

    public void setAvgRatting(Double avgRatting) {
        this.avgRatting = avgRatting;
    }

    @SerializedName("vendor_id")
    private Integer vendorId;

    @SerializedName("avg_ratting")
    private Double avgRatting;
}
