package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class OrderInfo {

    @SerializedName("date")
    private String date;

    @SerializedName("street_address")
    private String streetAddress;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("order_notes")
    private String orderNotes;

    @SerializedName("shipping_cost")
    private String shippingCost;

    @SerializedName("discount_amount")
    private Integer discountAmount;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("tax")
    private String tax;

    @SerializedName("coupon_name")
    private String couponName;

    @SerializedName("payment_type")
    private Integer paymentType;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("subtotal")
    private Integer subtotal;

    @SerializedName("grand_total")
    private String grandTotal;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private Integer status;

    public String getDate() {
        return date;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTax() {
        return tax;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getEmail() {
        return email;
    }

    public Integer getStatus() {
        return status;
    }
}
