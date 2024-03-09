package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class OrderRetuenRequestResponse {

    @SerializedName("data")
    private ArrayList<OrderRetuenRequestDataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    @SerializedName("order_info")
    private OrderRetuenRequestOrderInfo orderInfo;

    public ArrayList<OrderRetuenRequestDataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public OrderRetuenRequestOrderInfo getOrderInfo() {
        return orderInfo;
    }
}

