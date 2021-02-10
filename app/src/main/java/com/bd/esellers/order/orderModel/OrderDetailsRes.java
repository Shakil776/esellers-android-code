
package com.bd.esellers.order.orderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsRes {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("order_details")
    @Expose
    private OrderDetails orderDetails;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

}
