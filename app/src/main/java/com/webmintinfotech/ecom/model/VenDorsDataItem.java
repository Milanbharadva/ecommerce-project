package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VenDorsDataItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("rattings")
    private ArrayList<VenDorsRattings> rattings;

    @SerializedName("id")
    private Integer id;

    // Constructor, Getters, and Setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<VenDorsRattings> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<VenDorsRattings> rattings) {
        this.rattings = rattings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
