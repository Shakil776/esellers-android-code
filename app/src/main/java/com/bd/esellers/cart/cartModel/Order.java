
package com.bd.esellers.cart.cartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("order_total")
    @Expose
    private Double orderTotal;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("cart")
    @Expose
    private List<Cart> cart = null;
    @SerializedName("address")
    @Expose
    private Address address;


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
