package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BannerResponse {

    @SerializedName("bottombanner")
    private ArrayList<BottombannerItem> bottombanner;

    @SerializedName("topbanner")
    private ArrayList<TopbannerItem> topbanner;

    @SerializedName("popupbanner")
    private ArrayList<PopupbannerItem> popupbanner;

    @SerializedName("leftbanner")
    private ArrayList<LeftbannerItem> leftbanner;

    @SerializedName("largebanner")
    private ArrayList<LargebannerItem> largebanner;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    @SerializedName("sliders")
    private ArrayList<SlidersItem> sliders;

    // Getters and setters

    public ArrayList<BottombannerItem> getBottombanner() {
        return bottombanner;
    }

    public void setBottombanner(ArrayList<BottombannerItem> bottombanner) {
        this.bottombanner = bottombanner;
    }

    public ArrayList<TopbannerItem> getTopbanner() {
        return topbanner;
    }

    public void setTopbanner(ArrayList<TopbannerItem> topbanner) {
        this.topbanner = topbanner;
    }

    public ArrayList<PopupbannerItem> getPopupbanner() {
        return popupbanner;
    }

    public void setPopupbanner(ArrayList<PopupbannerItem> popupbanner) {
        this.popupbanner = popupbanner;
    }

    public ArrayList<LeftbannerItem> getLeftbanner() {
        return leftbanner;
    }

    public void setLeftbanner(ArrayList<LeftbannerItem> leftbanner) {
        this.leftbanner = leftbanner;
    }

    public ArrayList<LargebannerItem> getLargebanner() {
        return largebanner;
    }

    public void setLargebanner(ArrayList<LargebannerItem> largebanner) {
        this.largebanner = largebanner;
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

    public ArrayList<SlidersItem> getSliders() {
        return sliders;
    }

    public void setSliders(ArrayList<SlidersItem> sliders) {
        this.sliders = sliders;
    }
}

