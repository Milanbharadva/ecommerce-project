package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDataItem {

    @SerializedName("rattings")
    private ArrayList<ProductRattingsItem> rattings;

    @SerializedName("is_variation")
    private String isVariation;

    @SerializedName("id")
    private Integer id;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("sku")
    private String sku;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("is_wishlist")
    private Integer isWishlist;

    @SerializedName("productimage")
    private ProductProductimage productimage;

    @SerializedName("variation")
    private ProductVariation variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    public ArrayList<ProductRattingsItem> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<ProductRattingsItem> rattings) {
        this.rattings = rattings;
    }

    public String getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(String isVariation) {
        this.isVariation = isVariation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Integer isWishlist) {
        this.isWishlist = isWishlist;
    }

    public ProductProductimage getProductimage() {
        return productimage;
    }

    public void setProductimage(ProductProductimage productimage) {
        this.productimage = productimage;
    }

    public ProductVariation getVariation() {
        return variation;
    }

    public void setVariation(ProductVariation variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
// Getters and Setters for all fields
}
