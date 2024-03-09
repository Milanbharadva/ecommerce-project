package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class OrderDataItem {

    @SerializedName("shipping_cost")
    private String shippingCost;

    @SerializedName("price")
    private String price;

    @SerializedName("total")
    private String total;

    @SerializedName("status")
    private Integer status;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("discount_amount")
    private String discountAmount;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("qty")
    private Integer qty;

    @SerializedName("tax")
    private String tax;

    @SerializedName("id")
    private Integer id;

    @SerializedName("attribute")
    private String attribute;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("variation")
    private String variation;

    public String getShippingCost() {
        return shippingCost;
    }

    public String getPrice() {
        return price;
    }

    public String getTotal() {
        return total;
    }

    public Integer getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getQty() {
        return qty;
    }

    public String getTax() {
        return tax;
    }

    public Integer getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getProductName() {
        return productName;
    }

    public String getVariation() {
        return variation;
    }
}
