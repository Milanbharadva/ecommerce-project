package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BrandDetailsResponse {
    @SerializedName("data")
    private BrandData alldata;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Integer status;

    // Getters and setters


    public BrandData getAlldata() {
        return alldata;
    }

    public void setAlldata(BrandData alldata) {
        this.alldata = alldata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

