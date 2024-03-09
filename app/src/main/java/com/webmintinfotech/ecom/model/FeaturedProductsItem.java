package com.webmintinfotech.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeaturedProductsItem implements Parcelable {

    @SerializedName("rattings")
    private ArrayList<Rattings> rattings;

    @SerializedName("is_variation")
    private Integer isVariation;

    @SerializedName("id")
    private Integer id;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("sku")
    private String sku;

    @SerializedName("product_name")
    private String productName;

    public ArrayList<Rattings> getRattings() {
        return rattings;
    }

    public void setRattings(ArrayList<Rattings> rattings) {
        this.rattings = rattings;
    }

    public Integer getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(Integer isVariation) {
        this.isVariation = isVariation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Integer isWishlist) {
        this.isWishlist = isWishlist;
    }

    public Productimage getProductimage() {
        return productimage;
    }

    public void setProductimage(Productimage productimage) {
        this.productimage = productimage;
    }

    public Object getVariation() {
        return variation;
    }

    public void setVariation(Object variation) {
        this.variation = variation;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    @SerializedName("is_wishlist")
    private Integer isWishlist;

    @SerializedName("productimage")
    private Productimage productimage;

    @SerializedName("variation")
    private Object variation;

    @SerializedName("discounted_price")
    private String discountedPrice;

    private int isChecked;

    protected FeaturedProductsItem(Parcel in) {
        if (in.readByte() == 0) {
            isVariation = null;
        } else {
            isVariation = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        productPrice = in.readString();
        sku = in.readString();
        productName = in.readString();
        if (in.readByte() == 0) {
            isWishlist = null;
        } else {
            isWishlist = in.readInt();
        }
        discountedPrice = in.readString();
        isChecked = in.readInt();
    }

    public static final Creator<FeaturedProductsItem> CREATOR = new Creator<FeaturedProductsItem>() {
        @Override
        public FeaturedProductsItem createFromParcel(Parcel in) {
            return new FeaturedProductsItem(in);
        }

        @Override
        public FeaturedProductsItem[] newArray(int size) {
            return new FeaturedProductsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (isVariation == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isVariation);
        }
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(productPrice);
        parcel.writeString(sku);
        parcel.writeString(productName);
        if (isWishlist == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isWishlist);
        }
        parcel.writeString(discountedPrice);
        parcel.writeInt(isChecked);
    }

    // Constructor, getters, and setters
    // Implement Parcelable methods if necessary

}
