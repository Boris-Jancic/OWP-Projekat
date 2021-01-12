package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.Book;

import java.util.List;

public interface BookDao {

    public Book findOne(String isbn);

    public List<Book> findAll();

    public List<Book> find(String name, float minPrice, float maxPrice, String author, String language);

    public void save(Book book);

    public void update(Book book);

    public void delete(String isbn);
}
