package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorData {

    @SerializedName("first_page_url")
    private String firstPageUrl;

    @SerializedName("path")
    private String path;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("total")
    private Integer total;

    @SerializedName("data")
    private ArrayList<VendorDataItem> data;

    @SerializedName("last_page")
    private Integer lastPage;

    @SerializedName("last_page_url")
    private String lastPageUrl;

    @SerializedName("next_page_url")
    private Object nextPageUrl;

    @SerializedName("from")
    private Integer from;

    @SerializedName("to")
    private Integer to;

    @SerializedName("prev_page_url")
    private Object prevPageUrl;

    @SerializedName("current_page")
    private Integer currentPage;

    // Getters and setters
}
