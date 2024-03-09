package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BrandDetailsDataItem {
    @SerializedName("rattings")
    private ArrayList<BrandDetailsRattingsItem> rattings;
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
    private BrandDetailsProductimage productimage;
    @SerializedName("variation")
    private BrandDetailsVariation variation;
    @SerializedName("discounted_price")
    private String discountedPrice;

    // Getters and setters


    public ArrayList<BrandDetailsRattingsItem> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<BrandDetailsRattingsItem> rattings) {
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

    public BrandDetailsProductimage getProductimage() {
        return productimage;
    }

    public void setProductimage(BrandDetailsProductimage productimage) {
        this.productimage = productimage;
    }

    public BrandDetailsVariation getVariation() {
        return variation;
    }

    public void setVariation(BrandDetailsVariation variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
