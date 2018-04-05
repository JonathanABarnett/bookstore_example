package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.Payment;
import com.alaythiaproductions.bookstore.models.UserPayment;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
        payment.setType(userPayment.getType());
        payment.setHolderName(userPayment.getHolderName());
        payment.setCardNumber(userPayment.getCardNumber());
        payment.setExpiryMonth(userPayment.getExpiryMonth());
        payment.setExpiryYear(userPayment.getExpiryYear());
        payment.setCvc(payment.getCvc());

        return payment;
    }
}
