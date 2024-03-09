package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class TrackOrderResponse extends TrackOrderInfo{
    @SerializedName("ratting")
    private Integer ratting;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    @SerializedName("order_info")
    private TrackOrderInfo orderInfo;

    // Getters and setters


    public Integer getRatting() {
        return ratting;
    }

    public void setRatting(Integer ratting) {
        this.ratting = ratting;
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

    public TrackOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(TrackOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}

