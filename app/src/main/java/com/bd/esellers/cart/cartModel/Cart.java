
package com.bd.esellers.cart.cartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.bd.esellers.product.productModel.Product;

import java.io.Serializable;
import java.util.Objects;

public class Cart implements Serializable {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_quantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("product_price")
    @Expose
    private double productPrice;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;

    private Product product;

    private double totalCash;

    private String productImg;

    private String productName;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cart cart = (Cart) obj;
        return productId.equals(cart.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
