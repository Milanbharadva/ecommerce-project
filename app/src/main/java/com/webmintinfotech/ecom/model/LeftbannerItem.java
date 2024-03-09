package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class LeftbannerItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("cat_id")
    private String catId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("type")
    private String type;

    // Getters and setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
