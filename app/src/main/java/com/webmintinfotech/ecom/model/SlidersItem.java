package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class SlidersItem {

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("link")
    private String link;

    // Getters and setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
