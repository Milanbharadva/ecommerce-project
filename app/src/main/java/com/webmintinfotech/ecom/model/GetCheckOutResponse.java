package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GetCheckOutResponse {

    @SerializedName("cartdata")
    private ArrayList<CheckOutDataItem> checkoutdata;

    @SerializedName("data")
    private CheckOutData data;

    @SerializedName("message")
    private String message;

    @SerializedName("coupon_name")
    private Object couponName;

    @SerializedName("status")
    private Integer status;

    public ArrayList<CheckOutDataItem> getCheckoutdata() {
        return checkoutdata;
    }

    public void setCheckoutdata(ArrayList<CheckOutDataItem> checkoutdata) {
        this.checkoutdata = checkoutdata;
    }

    public CheckOutData getData() {
        return data;
    }

    public void setData(CheckOutData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getCouponName() {
        return couponName;
    }

    public void setCouponName(Object couponName) {
        this.couponName = couponName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

