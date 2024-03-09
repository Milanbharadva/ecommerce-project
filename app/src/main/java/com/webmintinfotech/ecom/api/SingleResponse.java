package com.webmintinfotech.ecom.api;

import com.google.gson.annotations.SerializedName;

public class SingleResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    // Constructors, getters, and setters can be added if needed

    // Constructor
    public SingleResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    // Getters and Setters
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
