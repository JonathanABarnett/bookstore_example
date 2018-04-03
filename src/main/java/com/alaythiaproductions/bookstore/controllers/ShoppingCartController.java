package com.alaythiaproductions.bookstore.controllers;

import com.alaythiaproductions.bookstore.models.CartItem;
import com.alaythiaproductions.bookstore.models.ShoppingCart;
import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.services.CartItemService;
import com.alaythiaproductions.bookstore.services.ShoppingCartService;
import com.alaythiaproductions.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping(value = "/cart")
    public String shoppingCart(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        ShoppingCart shoppingCart = user.getShoppingCart();

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        shoppingCartService.updateShoppingCart(shoppingCart);

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("user", user);
        model.addAttribute("title", "Shopping Cart");

        if (cartItemList.size() < 1) {
            model.addAttribute("emptyCart", true);
        }

        return "views/shoppingCart";
    }
}
