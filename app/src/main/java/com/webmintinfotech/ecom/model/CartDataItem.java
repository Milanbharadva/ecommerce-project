package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class CartDataItem {

    @SerializedName("price")
    private String price;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("qty")
    private Integer qty;

    @SerializedName("id")
    private Integer id;

    @SerializedName("attribute")
    private String attribute;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("variation")
    private String variation;

    // Getters and setters


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
