package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.CartItem;
import com.alaythiaproductions.bookstore.models.ShoppingCart;

import java.util.List;

public interface CartItemService {

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    CartItem updateCartItem(CartItem cartItem);
}
