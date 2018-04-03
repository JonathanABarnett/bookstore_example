package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.BookToCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.stereotype.Repository;

@Repository
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {
}
