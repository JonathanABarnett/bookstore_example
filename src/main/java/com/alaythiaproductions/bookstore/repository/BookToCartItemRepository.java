package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.BookToCartItem;
import com.alaythiaproductions.bookstore.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {

    void deleteByCartItem(CartItem cartItem);
}
