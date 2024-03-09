package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class VendorsResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("vendors")
    private VendorsData vendors;

    @SerializedName("status")
    private Integer status;

    // Constructor, Getters, and Setters


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VendorsData getVendors() {
        return vendors;
    }

    public void setVendors(VendorsData vendors) {
        this.vendors = vendors;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

