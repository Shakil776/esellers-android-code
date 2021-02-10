
package com.bd.esellers.mainApp.dataModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sliderImages")
    @Expose
    private List<SliderImage> sliderImages = null;

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

    public List<SliderImage> getSliderImages() {
        return sliderImages;
    }

    public void setSliderImages(List<SliderImage> sliderImages) {
        this.sliderImages = sliderImages;
    }

}
