
package com.bd.esellers.order.orderModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderDetails implements Serializable {

    @SerializedName("order")
    @Expose
    private CustomerOrder order;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("payment")
    @Expose
    private Payment payment;

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}
