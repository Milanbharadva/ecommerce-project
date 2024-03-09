package com.webmintinfotech.ecom.api;

public class RestResponse<T> {

    private T data;
    private String message;
    private String status;
    private String mobile;
    private String currency;
    private String min_order_amount;
    private String max_order_amount;
    private String max_order_qty;
    private String referral_amount;
    private String map;

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMin_order_amount() {
        return min_order_amount;
    }

    public String getMax_order_amount() {
        return max_order_amount;
    }

    public String getMax_order_qty() {
        return max_order_qty;
    }

    public String getReferral_amount() {
        return referral_amount;
    }

    public String getMap() {
        return map;
    }
}
