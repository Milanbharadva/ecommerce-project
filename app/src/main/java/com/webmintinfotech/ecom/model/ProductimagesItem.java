package com.webmintinfotech.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductimagesItem implements Parcelable {

    @SerializedName("image_name")
    private String imageName;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("id")
    private Integer id;

    protected ProductimagesItem(Parcel in) {
        // Read data from Parcel and initialize fields
    }

    public static final Creator<ProductimagesItem> CREATOR = new Creator<ProductimagesItem>() {
        @Override
        public ProductimagesItem createFromParcel(Parcel in) {
            return new ProductimagesItem(in);
        }

        @Override
        public ProductimagesItem[] newArray(int size) {
            return new ProductimagesItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageName);
        dest.writeString(imageUrl);
        dest.writeValue(productId);
        dest.writeValue(id);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
