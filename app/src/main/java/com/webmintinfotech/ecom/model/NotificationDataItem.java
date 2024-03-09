package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class NotificationDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("order_status")
    private Integer orderStatus;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("order_id")
    private Integer orderId;

    public String getDate() {
        return date;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Integer getOrderId() {
        return orderId;
    }
}
