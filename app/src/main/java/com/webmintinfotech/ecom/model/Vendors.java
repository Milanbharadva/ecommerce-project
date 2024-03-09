package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Vendors {

    @SerializedName("image_url")
    private String imageUrl;

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

    public ArrayList<Rattings> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<Rattings> rattings) {
        this.rattings = rattings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("rattings")
    private ArrayList<Rattings> rattings;

    @SerializedName("id")
    private Integer id;
}
