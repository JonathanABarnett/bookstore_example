package com.alaythiaproductions.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("title", "Bookstore Home");
        return "views/index";
    }
}
