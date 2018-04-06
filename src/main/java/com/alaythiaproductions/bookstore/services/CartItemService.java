package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.Book;
import com.alaythiaproductions.bookstore.models.CartItem;
import com.alaythiaproductions.bookstore.models.ShoppingCart;
import com.alaythiaproductions.bookstore.models.User;

import java.util.List;

public interface CartItemService {

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    CartItem findById(Long cartItemId);

    CartItem updateCartItem(CartItem cartItem);

    CartItem addBookToCartItem(Book book, User user, Integer qty);

    void removeCartItem(CartItem cartItem);

    CartItem save(CartItem cartItem);
}
