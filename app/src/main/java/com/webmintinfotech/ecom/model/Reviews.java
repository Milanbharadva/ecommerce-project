package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;

public class Reviews {

    @SerializedName("total")
    private Integer total;

    @SerializedName("five_ratting")
    private Integer fiveRatting;

    @SerializedName("three_ratting")
    private Integer threeRatting;

    @SerializedName("two_ratting")
    private Integer twoRatting;

    @SerializedName("one_ratting")
    private Integer oneRatting;

    @SerializedName("avg_ratting")
    private String avgRatting;

    @SerializedName("four_ratting")
    private Integer fourRatting;

    // Getters and Setters for all fields

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFiveRatting() {
        return fiveRatting;
    }

    public void setFiveRatting(Integer fiveRatting) {
        this.fiveRatting = fiveRatting;
    }

    public Integer getThreeRatting() {
        return threeRatting;
    }

    public void setThreeRatting(Integer threeRatting) {
        this.threeRatting = threeRatting;
    }

    public Integer getTwoRatting() {
        return twoRatting;
    }

    public void setTwoRatting(Integer twoRatting) {
        this.twoRatting = twoRatting;
    }

    public Integer getOneRatting() {
        return oneRatting;
    }

    public void setOneRatting(Integer oneRatting) {
        this.oneRatting = oneRatting;
    }

    public String getAvgRatting() {
        return avgRatting;
    }

    public void setAvgRatting(String avgRatting) {
        this.avgRatting = avgRatting;
    }

    public Integer getFourRatting() {
        return fourRatting;
    }

    public void setFourRatting(Integer fourRatting) {
        this.fourRatting = fourRatting;
    }
}
