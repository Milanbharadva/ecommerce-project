package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BrandResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("vendors")
    private BrandVendors vendors;

    @SerializedName("status")
    private Integer status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BrandVendors getVendors() {
        return vendors;
    }

    public void setVendors(BrandVendors vendors) {
        this.vendors = vendors;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

