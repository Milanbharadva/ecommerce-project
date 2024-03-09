package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GetVendorDetailsResponse {

    @SerializedName("vendordetails")
    private Vendordetails vendordetails;

    @SerializedName("data")
    private VendorData data;

    @SerializedName("banners")
    private ArrayList<TopbannerItem> bannerList;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    // Getters and setters
}

