package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.*;

public interface OrderService {

    Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
                      Payment payment, String shippingMethod, User user);

    Order findOne(Long id);
}
