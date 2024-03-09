package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class InnerSubCateDataItem {

    @SerializedName("innersubcategory_name")
    private String innersubcategoryName;

    @SerializedName("id")
    private Integer id;

    public String getInnersubcategoryName() {
        return innersubcategoryName;
    }

    public void setInnersubcategoryName(String innersubcategoryName) {
        this.innersubcategoryName = innersubcategoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
