package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FlutterWaveResponse {
    @SerializedName("data")
    private FlutterWaveData data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    // Getters and setters...


    public FlutterWaveData getData() {
        return data;
    }

    public void setData(FlutterWaveData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

