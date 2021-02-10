
package com.bd.esellers.order.reviewModel;

import com.bd.esellers.product.productModel.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReviewPostResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("review")
    @Expose
    private Review review;
    @SerializedName("avg_rating")
    @Expose
    private Double avgRating;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

}
