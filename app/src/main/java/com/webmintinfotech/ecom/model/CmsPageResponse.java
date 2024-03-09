package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class CmsPageResponse {
    @SerializedName("privacypolicy")
    private String privacypolicy;

    @SerializedName("termsconditions")
    private String termsconditions;

    @SerializedName("about")
    private String about;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    public String getPrivacypolicy() {
        return privacypolicy;
    }

    public void setPrivacypolicy(String privacypolicy) {
        this.privacypolicy = privacypolicy;
    }

    public String getTermsconditions() {
        return termsconditions;
    }

    public void setTermsconditions(String termsconditions) {
        this.termsconditions = termsconditions;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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
