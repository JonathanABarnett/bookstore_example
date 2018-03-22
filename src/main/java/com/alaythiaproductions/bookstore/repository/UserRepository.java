package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String string);

    User findByEmail(String email);

    //User createUser(User user, Set<UserRole> userRoles) throws Exception;
}
