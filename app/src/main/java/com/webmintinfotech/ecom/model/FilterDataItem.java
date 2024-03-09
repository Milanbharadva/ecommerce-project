package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilterDataItem {

    @SerializedName("is_hot")
    private Integer isHot;

    @SerializedName("rattings")
    private ArrayList<FilterRattingsItem> rattings;

    @SerializedName("is_variation")
    private String isVariation;

    @SerializedName("ratings_average")
    private String ratingsAverage;

    @SerializedName("id")
    private Integer id;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("sku")
    private String sku;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("slug")
    private String slug;

    @SerializedName("is_wishlist")
    private Integer isWishlist;

    @SerializedName("productimage")
    private FilterProductimage productimage;

    @SerializedName("variation")
    private FilterVariation variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public ArrayList<FilterRattingsItem> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<FilterRattingsItem> rattings) {
        this.rattings = rattings;
    }

    public String getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(String isVariation) {
        this.isVariation = isVariation;
    }

    public String getRatingsAverage() {
        return ratingsAverage;
    }

    public void setRatingsAverage(String ratingsAverage) {
        this.ratingsAverage = ratingsAverage;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Integer isWishlist) {
        this.isWishlist = isWishlist;
    }

    public FilterProductimage getProductimage() {
        return productimage;
    }

    public void setProductimage(FilterProductimage productimage) {
        this.productimage = productimage;
    }

    public FilterVariation getVariation() {
        return variation;
    }

    public void setVariation(FilterVariation variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    // Getters and Setters
}
