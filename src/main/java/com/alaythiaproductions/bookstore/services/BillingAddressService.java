package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.BillingAddress;
import com.alaythiaproductions.bookstore.models.UserBilling;

public interface BillingAddressService {

    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
