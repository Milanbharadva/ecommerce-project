package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class WalletResponse {

    @SerializedName("data")
    private WalletData data;

    @SerializedName("walletamount")
    private String walletamount;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    // Constructor, Getters, and Setters


    public WalletData getData() {
        return data;
    }

    public void setData(WalletData data) {
        this.data = data;
    }

    public String getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(String walletamount) {
        this.walletamount = walletamount;
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

