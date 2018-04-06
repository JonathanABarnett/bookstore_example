package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
