
package com.bd.esellers.order.orderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shipping {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("shipping_name")
    @Expose
    private String shippingName;
    @SerializedName("shipping_email")
    @Expose
    private String shippingEmail;
    @SerializedName("shipping_mobile")
    @Expose
    private String shippingMobile;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public void setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
    }

    public String getShippingMobile() {
        return shippingMobile;
    }

    public void setShippingMobile(String shippingMobile) {
        this.shippingMobile = shippingMobile;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
