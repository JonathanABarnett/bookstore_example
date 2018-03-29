package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.UserPayment;
import com.alaythiaproductions.bookstore.repository.UserPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Override
    public UserPayment findById(long id) {
       return userPaymentRepository.findOne(id);
    }

    @Override
    public void removeById(long id) {
        userPaymentRepository.delete(id);
    }


}
