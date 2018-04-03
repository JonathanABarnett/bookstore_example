package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>{
}
