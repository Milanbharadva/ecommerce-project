package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class ReviewDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("ratting")
    private String ratting;

    @SerializedName("comment")
    private String comment;

    @SerializedName("users")
    private Users users;

    // Getters and Setters for all fields


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
