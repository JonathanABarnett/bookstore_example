package com.alaythiaproductions.bookstore.services;

import com.alaythiaproductions.bookstore.models.Book;
import com.alaythiaproductions.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book findOne(Long id) {
        return bookRepository.findOne(id);
    }

    public List<Book> findByCategory(String category) {
        List<Book> bookList = bookRepository.findByCategory(category);

        List<Book> activeBookList = new ArrayList<>();

        for (Book book : bookList) {
            if (book.isActive()) {
                activeBookList.add(book);
            }
        }
        return activeBookList;
    }

    public List<Book> blurrySearch(String title){
        List<Book> bookList = bookRepository.findByTitleContaining(title);

        List<Book> activeBookList = new ArrayList<>();

        for (Book book : bookList) {
            if (book.isActive()) {
                activeBookList.add(book);
            }
        }
        return activeBookList;
    }
}
