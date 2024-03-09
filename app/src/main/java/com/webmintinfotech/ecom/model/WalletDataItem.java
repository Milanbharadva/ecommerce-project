package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class WalletDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("wallet")
    private String wallet;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("transaction_type")
    private String transactionType;

    @SerializedName("type")
    private String type;

    @SerializedName("username")
    private Object username;

    // Constructor, Getters, and Setters


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }
}
