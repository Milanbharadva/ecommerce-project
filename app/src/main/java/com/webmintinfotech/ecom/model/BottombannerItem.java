package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class BottombannerItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("product_id")
    private Object productId;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Object getProductId() {
        return productId;
    }

    public void setProductId(Object productId) {
        this.productId = productId;
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
