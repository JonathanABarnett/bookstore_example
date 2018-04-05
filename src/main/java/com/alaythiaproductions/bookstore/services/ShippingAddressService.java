package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.ShippingAddress;
import com.alaythiaproductions.bookstore.models.UserShipping;

public interface ShippingAddressService {

    ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
