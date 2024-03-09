package com.webmintinfotech.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GetProductDetailsResponse {

    @SerializedName("data")
    private ProductDetailsData data;

    @SerializedName("returnpolicy")
    private Returnpolicy returnpolicy;

    @SerializedName("message")
    private String message;

    @SerializedName("vendors")
    private Vendors vendors;

    @SerializedName("related_products")
    private ArrayList<RelatedProductsItem> relatedProducts;

    @SerializedName("status")
    private Integer status;

    public ProductDetailsData getData() {
        return data;
    }

    public void setData(ProductDetailsData data) {
        this.data = data;
    }

    public Returnpolicy getReturnpolicy() {
        return returnpolicy;
    }

    public void setReturnpolicy(Returnpolicy returnpolicy) {
        this.returnpolicy = returnpolicy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Vendors getVendors() {
        return vendors;
    }

    public void setVendors(Vendors vendors) {
        this.vendors = vendors;
    }

    public ArrayList<RelatedProductsItem> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(ArrayList<RelatedProductsItem> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

