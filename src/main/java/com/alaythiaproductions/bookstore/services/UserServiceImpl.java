package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.security.PasswordResetToken;
import com.alaythiaproductions.bookstore.models.security.Role;
import com.alaythiaproductions.bookstore.models.security.UserRole;
import com.alaythiaproductions.bookstore.repository.PasswordResetTokenRepository;
import com.alaythiaproductions.bookstore.repository.RoleRepository;
import com.alaythiaproductions.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokeForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles)  {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (null != localUser) {
           LOG.info("user {} User already exists.", user.getUsername());
        } else {
            for (UserRole userRole : userRoles) {
                roleRepository.save(userRole.getRole());
            }
            user.getUserRoles().addAll(userRoles);

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
