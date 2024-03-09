package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendordetailsData {

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("store_address")
    private String storeAddress;

    @SerializedName("rattings")
    private ArrayList<VendorsDetailsRattingsItem> rattings;

    @SerializedName("email")
    private String email;

    // Constructor, Getters, and Setters


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public ArrayList<VendorsDetailsRattingsItem> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<VendorsDetailsRattingsItem> rattings) {
        this.rattings = rattings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
