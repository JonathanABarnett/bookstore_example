package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.UserPayment;

public interface UserPaymentService {

    UserPayment findById(long id);
}
