package com.bd.esellers.interfaces;

import com.bd.esellers.cart.cartModel.Cart;


public interface AddorRemoveCallbacks {

    void onAddProduct(Cart cart);

    void onRemoveProduct(Cart cart);


}
