package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class PopupbannerItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private Object productId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("cat_id")
    private Integer catId;

    @SerializedName("type")
    private String type;

    // Getters and setters
}
