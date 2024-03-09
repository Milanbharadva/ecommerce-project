package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class SearchProductResponse {
    @SerializedName("data")
    private ArrayList<SearchDataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    // Getters and setters


    public ArrayList<SearchDataItem> getData() {
        return data;
    }

    public void setData(ArrayList<SearchDataItem> data) {
        this.data = data;
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

