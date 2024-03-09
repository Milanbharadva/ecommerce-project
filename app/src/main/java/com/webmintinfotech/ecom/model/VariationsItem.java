package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class VariationsItem {

    @SerializedName("price")
    private String price;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("qty")
    private String qty;

    @SerializedName("id")
    private Integer id;

    @SerializedName("discounted_variation_price")
    private String discountedVariationPrice;

    @SerializedName("variation")
    private String variation;

    @SerializedName("isSelect")
    private Boolean isSelect;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiscountedVariationPrice() {
        return discountedVariationPrice;
    }

    public void setDiscountedVariationPrice(String discountedVariationPrice) {
        this.discountedVariationPrice = discountedVariationPrice;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
