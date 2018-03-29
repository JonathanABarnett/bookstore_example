package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {

}
