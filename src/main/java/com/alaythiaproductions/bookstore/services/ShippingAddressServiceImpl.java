package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.ShippingAddress;
import com.alaythiaproductions.bookstore.models.UserShipping;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Override
    public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress){
        shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
        shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
        shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
        shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
        shippingAddress.setShippingAddressState(userShipping.getUserShippingState());
        shippingAddress.setShippingAddressZipcode(userShipping.getUserShippingZipcode());
        shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());

        return shippingAddress;
    }
}
