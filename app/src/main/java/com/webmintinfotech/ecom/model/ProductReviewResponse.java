package com.webmintinfotech.ecom.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ProductReviewResponse {

    @SerializedName("reviews")
    private Reviews reviews;

    @SerializedName("all_review")
    private AllReview allReview;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Integer status;

    public Reviews getReviews() {
        return reviews;
    }

    public AllReview getAllReview() {
        return allReview;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}

