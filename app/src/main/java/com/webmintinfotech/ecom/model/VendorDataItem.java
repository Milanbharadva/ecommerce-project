package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorDataItem {

    @SerializedName("rattings")
    private ArrayList<Rattings> rattings;

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
    private VendorProductimage productimage;

    @SerializedName("variation")
    private VendorVariation variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    // Getters and setters
}
