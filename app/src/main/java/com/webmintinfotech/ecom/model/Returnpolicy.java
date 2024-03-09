package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class Returnpolicy {

    @SerializedName("return_policies")
    private String returnPolicies;

    public String getReturnPolicies() {
        return returnPolicies;
    }

    public void setReturnPolicies(String returnPolicies) {
        this.returnPolicies = returnPolicies;
    }
}
