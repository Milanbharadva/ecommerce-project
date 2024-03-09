package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class CheckOutDataItem {

    @SerializedName("shipping_cost")
    private String shippingCost;

    @SerializedName("price")
    private String price;

    @SerializedName("total")
    private String total;

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

    @SerializedName("vendor_id")
    private Integer vendorId;

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }
}
