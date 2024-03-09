package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class AddressData {
    @SerializedName("street_address")
    private String streetAddress;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private Integer id;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("email")
    private String email;

    // Getters and setters...


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
