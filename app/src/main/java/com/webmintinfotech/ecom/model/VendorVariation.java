package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class VendorVariation {

    @SerializedName("price")
    private String price;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("qty")
    private String qty;

    @SerializedName("id")
    private Integer id;

    @SerializedName("discounted_variation_price")
    private String discountedVariationPrice;

    @SerializedName("variation")
    private String variation;

    // Getters and setters
}
