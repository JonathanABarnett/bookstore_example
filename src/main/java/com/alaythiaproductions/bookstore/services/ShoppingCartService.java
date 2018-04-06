package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
