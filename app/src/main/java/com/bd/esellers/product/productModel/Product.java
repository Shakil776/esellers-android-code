
package com.bd.esellers.product.productModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.bd.esellers.mainApp.dataModel.RecyclerViewItem;

public class Product extends RecyclerViewItem implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturerId;
//    @SerializedName("vendor_id")
//    @Expose
//    private String vendorId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("rating_count")
    @Expose
    private String ratingCount;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("status_id")
    @Expose
    private String statusId;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("long_description")
    @Expose
    private String longDescription;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("publication_status")
    @Expose
    private String publicationStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("manufacturer_name")
    @Expose
    private String manufacturerName;
//    @SerializedName("vendor_name")
//    @Expose
//    private String vendorName;
    @SerializedName("product_status_name")
    @Expose
    private String productStatusName;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

    private boolean addedToCart = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

//    public String getVendorId() {
//        return vendorId;
//    }
//
//    public void setVendorId(String vendorId) {
//        this.vendorId = vendorId;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

//    public String getVendorName() {
//        return vendorName;
//    }
//
//    public void setVendorName(String vendorName) {
//        this.vendorName = vendorName;
//    }

    public String getProductStatusName() {
        return productStatusName;
    }

    public void setProductStatusName(String productStatusName) {
        this.productStatusName = productStatusName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isAddedToCart() {
        return addedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        this.addedToCart = addedToCart;
    }
}
