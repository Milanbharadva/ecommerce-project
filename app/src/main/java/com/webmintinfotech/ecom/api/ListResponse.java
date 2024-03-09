package com.webmintinfotech.ecom.api;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ListResponse<T> {
    @SerializedName("data")
    private ArrayList<T> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    @SerializedName("walletamount")
    private String walletamount;

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(String walletamount) {
        this.walletamount = walletamount;
    }
}
