package com.alaythiaproductions.bookstore.controllers;

import com.alaythiaproductions.bookstore.models.Book;
import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.services.BookService;
import com.alaythiaproductions.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/searchByCategory")
    public String searchByCategory(@RequestParam("category") String category, Model model, Principal principal){
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        String classActiveCategory = "active" + category;
        classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
        classActiveCategory = classActiveCategory.replaceAll("&", "");

        model.addAttribute(classActiveCategory, true);

        List<Book> bookList = bookService.findByCategory(category);

        if (bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "views/bookshelf";
        }

        model.addAttribute("books", bookList);

        return "views/bookshelf";
    }

    @PostMapping(value = "/searchBook")
    public String searchBook(@ModelAttribute("keyword") String keyword, Principal principal, Model model) {

        System.out.println("Keyword:"  + keyword);
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Book> bookList = bookService.blurrySearch(keyword);

        for (Book book : bookList) {
            System.out.println(book.getTitle());
        }

        if (bookList.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "views/bookshelf";
        }

        model.addAttribute("books", bookList);

        return "views/bookshelf";
    }
}
