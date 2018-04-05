package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.Payment;
import com.alaythiaproductions.bookstore.models.UserPayment;

public interface PaymentService {
    Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
