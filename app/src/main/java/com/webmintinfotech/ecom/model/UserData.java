package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("profile_pic")
    private String profilePic;

    @SerializedName("id")
    private Integer id;

    @SerializedName("notification_status")
    private Integer notificationStatus;

    @SerializedName("referral_code")
    private String referralCode;

    @SerializedName("email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Integer notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
