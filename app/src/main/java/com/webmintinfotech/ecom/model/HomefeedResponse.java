package com.webmintinfotech.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class HomefeedResponse implements Parcelable {

    @SerializedName("featured_products")
    private ArrayList<FeaturedProductsItem> featuredProducts;

    @SerializedName("brands")
    private ArrayList<BrandsItem> brands;

    @SerializedName("new_products")
    private ArrayList<NewProductsItem> newProducts;

    @SerializedName("currency")
    private String currency;

    @SerializedName("hot_products")
    private ArrayList<HotProductsItem> hotProducts;

    @SerializedName("message")
    private String message;

    @SerializedName("currency_position")
    private String currencyPosition;

    @SerializedName("referral_amount")
    private String referralAmount;

    @SerializedName("vendors")
    private ArrayList<VendorsItem> vendors;

    public ArrayList<FeaturedProductsItem> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(ArrayList<FeaturedProductsItem> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public ArrayList<BrandsItem> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<BrandsItem> brands) {
        this.brands = brands;
    }

    public ArrayList<NewProductsItem> getNewProducts() {
        return newProducts;
    }

    public void setNewProducts(ArrayList<NewProductsItem> newProducts) {
        this.newProducts = newProducts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<HotProductsItem> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(ArrayList<HotProductsItem> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrencyPosition() {
        return currencyPosition;
    }

    public void setCurrencyPosition(String currencyPosition) {
        this.currencyPosition = currencyPosition;
    }

    public String getReferralAmount() {
        return referralAmount;
    }

    public void setReferralAmount(String referralAmount) {
        this.referralAmount = referralAmount;
    }

    public ArrayList<VendorsItem> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<VendorsItem> vendors) {
        this.vendors = vendors;
    }

    public Integer getNotifications() {
        return notifications;
    }

    public void setNotifications(Integer notifications) {
        this.notifications = notifications;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @SerializedName("notifications")
    private Integer notifications;

    @SerializedName("status")
    private Integer status;

    protected HomefeedResponse(Parcel in) {
        featuredProducts = in.createTypedArrayList(FeaturedProductsItem.CREATOR);
        currency = in.readString();
        message = in.readString();
        currencyPosition = in.readString();
        referralAmount = in.readString();
        if (in.readByte() == 0) {
            notifications = null;
        } else {
            notifications = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
    }

    public static final Creator<HomefeedResponse> CREATOR = new Creator<HomefeedResponse>() {
        @Override
        public HomefeedResponse createFromParcel(Parcel in) {
            return new HomefeedResponse(in);
        }

        @Override
        public HomefeedResponse[] newArray(int size) {
            return new HomefeedResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(featuredProducts);
        parcel.writeString(currency);
        parcel.writeString(message);
        parcel.writeString(currencyPosition);
        parcel.writeString(referralAmount);
        if (notifications == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(notifications);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
    }

    // Constructor, getters, and setters
    // Implement Parcelable methods if necessary

}

