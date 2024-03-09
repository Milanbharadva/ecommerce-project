package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class VendorsDetailsResponse {

    @SerializedName("vendordetails")
    private VendordetailsData vendordetails;

    @SerializedName("data")
    private VendorsDetailsData data;

    @SerializedName("banners")
    private ArrayList<TopbannerItem> bannerList;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    // Constructor, Getters, and Setters


    public VendordetailsData getVendordetails() {
        return vendordetails;
    }

    public void setVendordetails(VendordetailsData vendordetails) {
        this.vendordetails = vendordetails;
    }

    public VendorsDetailsData getData() {
        return data;
    }

    public void setData(VendorsDetailsData data) {
        this.data = data;
    }

    public ArrayList<TopbannerItem> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<TopbannerItem> bannerList) {
        this.bannerList = bannerList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

