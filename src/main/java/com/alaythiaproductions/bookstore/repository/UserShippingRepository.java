package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.UserShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserShippingRepository extends JpaRepository<UserShipping, Long> {
}
