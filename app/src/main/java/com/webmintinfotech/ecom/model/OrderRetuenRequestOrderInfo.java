package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class OrderRetuenRequestOrderInfo {

    @SerializedName("price")
    private String price;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("vendor_id")
    private String vendorId;

    @SerializedName("qty")
    private String qty;

    @SerializedName("id")
    private Integer id;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("variation")
    private String variation;

    @SerializedName("status")
    private String status;

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getQty() {
        return qty;
    }

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getVariation() {
        return variation;
    }

    public String getStatus() {
        return status;
    }
}
