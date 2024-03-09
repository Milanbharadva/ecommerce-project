package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Integer id;

    // Getters and Setters for all fields


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
