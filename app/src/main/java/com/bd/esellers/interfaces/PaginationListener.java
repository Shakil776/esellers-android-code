package com.bd.esellers.interfaces;


import com.bd.esellers.product.productModel.Product;

import java.util.List;

public interface PaginationListener {

    void add(Product product);

    void addAllItem(List<Product> products);

    void removeItem(Product product);

    void clearList();

    boolean isEmpty();

    void addLoadingFooter();

    void removeLoadingFooter();

    Product getItem(int position);

}
