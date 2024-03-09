package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class OrderDetailsResponse {

    @SerializedName("order_data")
    private ArrayList<OrderDataItem> orderData;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    @SerializedName("order_info")
    private OrderInfo orderInfo;

    public ArrayList<OrderDataItem> getOrderData() {
        return orderData;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }
}

