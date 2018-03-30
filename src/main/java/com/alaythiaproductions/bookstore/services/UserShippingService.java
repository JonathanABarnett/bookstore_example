package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.UserShipping;

public interface UserShippingService {

    UserShipping findById(long id);

    void removeById(long id);
}
