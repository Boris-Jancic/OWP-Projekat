package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.Book;

import java.util.List;

public interface BookService {

    Book findOne(String isbn);

    List<Book> findAll();

    List<Book> find(String isbn, String name, float minPrice, float maxPrice, String author, String language);

    void save(Book user);

    void update(Book user);

    void delete(String isbn);
}
