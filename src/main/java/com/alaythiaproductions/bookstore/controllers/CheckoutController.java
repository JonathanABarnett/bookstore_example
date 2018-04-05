package com.alaythiaproductions.bookstore.controllers;

import com.alaythiaproductions.bookstore.models.*;
import com.alaythiaproductions.bookstore.services.*;
import com.alaythiaproductions.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BillingAddressService billingAddressService;


    private ShippingAddress shippingAddress = new ShippingAddress();
    private BillingAddress billingAddress = new BillingAddress();
    private Payment payment = new Payment();

    @RequestMapping(value = "/checkout")
    public String checkout(@RequestParam(value = "id") Long cartId, @RequestParam(value = "missingRequiredField", required = false) boolean missingRequireField,
                           Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());

        if (cartId != user.getShoppingCart().getId()) {
            return "badRequestPage";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        if (cartItemList.size() == 0) {
            model.addAttribute("emptyCart", true);
            return "forward:/shoppingCart/cart";
        }

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getBook().getNumberOfStock() < cartItem.getQty()) {
                model.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }


        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);

        if (userPaymentList.size() == 0) {
            model.addAttribute("emptyPaymentList", true);
        } else {
            model.addAttribute("emptyPaymentList", false);
        }

        if (userShippingList.size() == 0) {
            model.addAttribute("emptyShippingList", true);
        } else {
            model.addAttribute("emptyShippingList", false);
        }

        ShoppingCart shoppingCart = user.getShoppingCart();

        for (UserShipping userShipping : userShippingList) {
            if (userShipping.isUserShippingDefault()) {
                shippingAddressService.setByUserShipping(userShipping, shippingAddress);
            }
        }

        for (UserPayment userPayment : userPaymentList) {
            if (userPayment.isDefaultPayment()) {
                paymentService.setByUserPayment(userPayment, payment);
                billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
            }
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        List<String> stateList = USConstants.listOfUSStateCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);

        model.addAttribute("classActiveShipping", true);

        if (missingRequireField) {
            model.addAttribute("missingRequiredField", true);
        }

        return "views/checkout";
    }
}
