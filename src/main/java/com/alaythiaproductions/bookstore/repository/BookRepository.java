package com.alaythiaproductions.bookstore.repository;

import com.alaythiaproductions.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


}
