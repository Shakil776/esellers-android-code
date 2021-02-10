package com.bd.esellers.mainApp.dataModel;

import com.bd.esellers.product.productModel.Product;

import java.io.Serializable;

public class HorizontalModel extends RecyclerViewItem implements Serializable {
    private Product product;
    private String category;
    private String status;

    public HorizontalModel(Product product) {
        this.product = product;
        category = product.getCategoryName();
        status = product.getProductStatusName();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
