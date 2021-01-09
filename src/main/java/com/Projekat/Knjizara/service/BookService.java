package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.Book;

import java.util.List;

public interface BookService {

    Book findOne(String isbn);

    List<Book> findAll(String isbn);

    List<Book> find(String name, String author, String language);

    void save(Book user);

    void update(Book user);

    void delete(String isbn);
}
