package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewProductsItem {

    @SerializedName("rattings")
    private ArrayList<Rattings> rattings;

    @SerializedName("is_variation")
    private Integer isVariation;

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
    private Productimage productimage;

    @SerializedName("variation")
    private Variation variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    // Constructor, getters, and setters


    public ArrayList<Rattings> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<Rattings> rattings) {
        this.rattings = rattings;
    }

    public Integer getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(Integer isVariation) {
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

    public Productimage getProductimage() {
        return productimage;
    }

    public void setProductimage(Productimage productimage) {
        this.productimage = productimage;
    }

    public Variation getVariation() {
        return variation;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
