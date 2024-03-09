package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class PaymentListResponse {

    @SerializedName("walletamount")
    private String walletamount;

    @SerializedName("message")
    private String message;

    @SerializedName("paymentlist")
    private ArrayList<PaymentlistItem> paymentlist;

    @SerializedName("status")
    private Integer status;

    public String getWalletamount() {
        return walletamount;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<PaymentlistItem> getPaymentlist() {
        return paymentlist;
    }

    public Integer getStatus() {
        return status;
    }
}

