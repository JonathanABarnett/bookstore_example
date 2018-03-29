package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.UserPayment;

public interface UserPaymentService {

    UserPayment findById(long id);

    void removeById(long id);

}
