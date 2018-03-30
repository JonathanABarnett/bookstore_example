package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.UserShipping;
import com.alaythiaproductions.bookstore.repository.UserShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Override
    public UserShipping findById(long id){
        return userShippingRepository.findOne(id);
    }

    @Override
    public void removeById(long id) {
        userShippingRepository.delete(id);
    }
}
