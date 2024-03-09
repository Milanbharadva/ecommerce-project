package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDetailsData {

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("free_shipping")
    private Integer freeShipping;

    @SerializedName("rattings")
    private ArrayList<Rattings> rattings;

    @SerializedName("description")
    private String description;

    @SerializedName("is_variation")
    private Integer isVariation;

    @SerializedName("subcategory_name")
    private String subcategoryName;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("is_wishlist")
    private Integer isWishlist;

    @SerializedName("discounted_price")
    private String discountedPrice;

    @SerializedName("is_return")
    private Integer isReturn;

    @SerializedName("variations")
    private ArrayList<VariationsItem> variations;

    @SerializedName("innersubcategory_name")
    private String innersubcategoryName;

    @SerializedName("cat_id")
    private Integer catId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("attribute")
    private String attribute;

    @SerializedName("sku")
    private String sku;

    @SerializedName("return_days")
    private String returnDays;

    @SerializedName("shipping_cost")
    private String shippingCost;

    @SerializedName("tax")
    private String tax;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("est_shipping_days")
    private String estShippingDays;

    @SerializedName("tax_type")
    private String taxType;

    @SerializedName("vendor_id")
    private Integer vendorId;

    @SerializedName("productimages")
    private ArrayList<ProductimagesItem> productimages;

    @SerializedName("product_qty")
    private String availableStock;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Integer freeShipping) {
        this.freeShipping = freeShipping;
    }

    public ArrayList<Rattings> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<Rattings> rattings) {
        this.rattings = rattings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(Integer isVariation) {
        this.isVariation = isVariation;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Integer isWishlist) {
        this.isWishlist = isWishlist;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public ArrayList<VariationsItem> getVariations() {
        return variations;
    }

    public void setVariations(ArrayList<VariationsItem> variations) {
        this.variations = variations;
    }

    public String getInnersubcategoryName() {
        return innersubcategoryName;
    }

    public void setInnersubcategoryName(String innersubcategoryName) {
        this.innersubcategoryName = innersubcategoryName;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(String returnDays) {
        this.returnDays = returnDays;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEstShippingDays() {
        return estShippingDays;
    }

    public void setEstShippingDays(String estShippingDays) {
        this.estShippingDays = estShippingDays;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public ArrayList<ProductimagesItem> getProductimages() {
        return productimages;
    }

    public void setProductimages(ArrayList<ProductimagesItem> productimages) {
        this.productimages = productimages;
    }

    public String getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(String availableStock) {
        this.availableStock = availableStock;
    }
}
