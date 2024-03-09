package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class OrderHistoryResponse {

    @SerializedName("data")
    private OrderHistoryData data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    public OrderHistoryData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}

