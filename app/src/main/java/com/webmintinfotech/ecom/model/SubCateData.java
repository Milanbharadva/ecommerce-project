package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCateData {
    @SerializedName("subcategory")
    private ArrayList<SubcategoryItem> subcategory;

    // Getters and setters


    public ArrayList<SubcategoryItem> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<SubcategoryItem> subcategory) {
        this.subcategory = subcategory;
    }
}
