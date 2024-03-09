package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderHistoryData {

    @SerializedName("first_page_url")
    private String firstPageUrl;

    @SerializedName("path")
    private String path;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("total")
    private Integer total;

    @SerializedName("data")
    private ArrayList<OrderHistoryDataItem> data;

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

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public String getPath() {
        return path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public ArrayList<OrderHistoryDataItem> getData() {
        return data;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
