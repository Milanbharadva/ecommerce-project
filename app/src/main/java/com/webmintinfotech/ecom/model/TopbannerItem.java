package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class TopbannerItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("cat_id")
    private Integer catId;

    @SerializedName("type")
    private String type;

    // Getters and setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
