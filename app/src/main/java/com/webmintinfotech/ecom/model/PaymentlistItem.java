package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class PaymentlistItem {

    @SerializedName("live_secret_key")
    private String liveSecretKey;

    @SerializedName("environment")
    private Integer environment;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("live_public_key")
    private String livePublicKey;

    @SerializedName("test_public_key")
    private String testPublicKey;

    @SerializedName("encryption_key")
    private String encryptionKey;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("payment_name")
    private String paymentName;

    @SerializedName("test_secret_key")
    private String testSecretKey;

    @SerializedName("id")
    private Integer id;

    @SerializedName("status")
    private Integer status;

    @SerializedName("isSelect")
    private Boolean isSelect;

    public String getLiveSecretKey() {
        return liveSecretKey;
    }

    public Integer getEnvironment() {
        return environment;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getLivePublicKey() {
        return livePublicKey;
    }

    public String getTestPublicKey() {
        return testPublicKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public String getTestSecretKey() {
        return testSecretKey;
    }

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }
}
