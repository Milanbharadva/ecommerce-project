package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchDataItem {
    @SerializedName("rattings")
    private ArrayList<SearchRattingsItem> rattings;

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
    private String isWishlist;

    @SerializedName("productimage")
    private SearchProductimage productimage;

    @SerializedName("variation")
    private SearchVariation variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    // Getters and setters


    public ArrayList<SearchRattingsItem> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<SearchRattingsItem> rattings) {
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

    public String getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(String isWishlist) {
        this.isWishlist = isWishlist;
    }

    public SearchProductimage getProductimage() {
        return productimage;
    }

    public void setProductimage(SearchProductimage productimage) {
        this.productimage = productimage;
    }

    public SearchVariation getVariation() {
        return variation;
    }

    public void setVariation(SearchVariation variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
