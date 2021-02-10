
package com.bd.esellers.cart.cartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

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

}
