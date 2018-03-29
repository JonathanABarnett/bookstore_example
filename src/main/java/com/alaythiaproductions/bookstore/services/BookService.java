package com.alaythiaproductions.bookstore.services;


import com.alaythiaproductions.bookstore.models.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findOne(Long id);

}
