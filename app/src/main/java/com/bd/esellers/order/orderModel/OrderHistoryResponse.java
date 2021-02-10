package com.bd.esellers.order.orderModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderHistoryResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("orders")
    @Expose
    private List<CustomerOrder> orders = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }

}

