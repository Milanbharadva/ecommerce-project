package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubcategoryItem {
    @SerializedName("subcat_id")
    private Integer subcatId;

    @SerializedName("subcategory_name")
    private String subcategoryName;

    @SerializedName("innersubcategory")
    private ArrayList<InnersubcategoryItem> innersubcategory;

    private boolean expand = false;

    // Getters and setters


    public Integer getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(Integer subcatId) {
        this.subcatId = subcatId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public ArrayList<InnersubcategoryItem> getInnersubcategory() {
        return innersubcategory;
    }

    public void setInnersubcategory(ArrayList<InnersubcategoryItem> innersubcategory) {
        this.innersubcategory = innersubcategory;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
