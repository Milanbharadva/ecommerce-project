package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class CouponDataItem {

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("amount")
    private Object amount;

    @SerializedName("percentage")
    private String percentage;

    @SerializedName("type")
    private String type;

    @SerializedName("coupon_name")
    private String couponName;

    @SerializedName("min_amount")
    private String min_amount;

    @SerializedName("description")
    private String description;

    @SerializedName("start_date")
    private String startDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
