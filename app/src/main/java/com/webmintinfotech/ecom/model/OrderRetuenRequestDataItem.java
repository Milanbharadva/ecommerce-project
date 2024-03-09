package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class OrderRetuenRequestDataItem {

    @SerializedName("return_conditions")
    private String returnConditions;

    @SerializedName("isSelect")
    private Boolean isSelect;

    public String getReturnConditions() {
        return returnConditions;
    }

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }
}
