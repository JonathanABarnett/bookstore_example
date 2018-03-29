package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.UserPayment;
import com.alaythiaproductions.bookstore.repository.UserPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Override
    public UserPayment findById(long id) {
       return userPaymentRepository.findOne(id);
    }


}
