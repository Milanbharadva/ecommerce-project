package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GetWishListResponse {

    @SerializedName("data")
    private WishListData allData;

    @SerializedName("message")
    private String message;

    public WishListData getAllData() {
        return allData;
    }

    public void setAllData(WishListData allData) {
        this.allData = allData;
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

    @SerializedName("status")
    private Integer status;

    // Getters and setters
}

