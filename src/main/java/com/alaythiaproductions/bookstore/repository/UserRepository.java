package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername (String string);
}
