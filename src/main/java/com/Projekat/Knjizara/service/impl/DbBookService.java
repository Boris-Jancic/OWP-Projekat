package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.BookDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbBookService implements BookService {

    @Autowired
    private BookDao bookDao;


    @Override
    public Book findOne(String isbn) {
        return bookDao.findOne(isbn);
    }

    @Override
    public List<Book> findAll(String isbn) {
        return bookDao.findAll();
    }

    @Override
    public List<Book> find(String name, String author, String language) {
        return bookDao.find(name, author, language);
    }

    @Override
    public void save(Book book) {
        bookDao.save(book);
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public void delete(String isbn) {
        bookDao.delete(isbn);
    }
}
