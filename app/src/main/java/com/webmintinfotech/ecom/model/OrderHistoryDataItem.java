package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("payment_type")
    private Integer paymentType;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("id")
    private Integer id;

    @SerializedName("grand_total")
    private String grandTotal;

    @SerializedName("status")
    private Integer status;

    public String getDate() {
        return date;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public Integer getStatus() {
        return status;
    }
}
