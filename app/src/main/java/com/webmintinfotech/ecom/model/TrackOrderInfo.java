package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class TrackOrderInfo {
    @SerializedName("shipped_att")
    private String shippedAt;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("created_att")
    private String createdAt;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("variation")
    private String variation;

    @SerializedName("vendor_comment")
    private String vendorComment;

    @SerializedName("price")
    private String price;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("vendor_id")
    private String vendorId;

    @SerializedName("qty")
    private String qty;

    @SerializedName("id")
    private Integer id;

    @SerializedName("return_number")
    private String returnNumber;

    @SerializedName("confirmed_att")
    private String confirmedAt;

    @SerializedName("delivered_att")
    private String deliveredAt;

    @SerializedName("status")
    private Integer status;

    // Getters and setters


    public String getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(String shippedAt) {
        this.shippedAt = shippedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getVendorComment() {
        return vendorComment;
    }

    public void setVendorComment(String vendorComment) {
        this.vendorComment = vendorComment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public String getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(String confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
