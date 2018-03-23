package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.security.PasswordResetToken;
import com.alaythiaproductions.bookstore.models.security.UserRole;
import com.alaythiaproductions.bookstore.repository.UserRepository;

import java.util.Set;

public interface UserService {

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokeForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);
}
