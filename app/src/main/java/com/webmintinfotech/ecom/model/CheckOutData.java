package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class CheckOutData {

    @SerializedName("shipping_cost")
    private String shippingCost;

    @SerializedName("subtotal")
    private Integer subtotal;

    @SerializedName("tax")
    private String tax;

    @SerializedName("discount_amount")
    private String discount_amount;

    @SerializedName("grand_total")
    private String grand_total;

    @SerializedName("id")
    private Integer id;

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
