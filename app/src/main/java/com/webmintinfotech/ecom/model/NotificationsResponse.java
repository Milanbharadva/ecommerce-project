package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class NotificationsResponse {

    @SerializedName("data")
    private NotificationData data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    public NotificationData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}

