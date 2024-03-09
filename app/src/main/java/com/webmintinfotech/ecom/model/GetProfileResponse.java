package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GetProfileResponse  {

    @SerializedName("data")
    private UserData data;

    @SerializedName("contactinfo")
    private ContactInfo contactinfo;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public ContactInfo getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(ContactInfo contactinfo) {
        this.contactinfo = contactinfo;
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


