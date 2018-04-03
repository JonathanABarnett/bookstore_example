package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.CartItem;
import com.alaythiaproductions.bookstore.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}
