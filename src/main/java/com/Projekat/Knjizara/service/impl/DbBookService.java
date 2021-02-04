package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.BookDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Discount;
import com.Projekat.Knjizara.models.WishListItem;
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
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public List<Book> find(String isbn, String name, float minPrice, float maxPrice, String author, String language) {
        return bookDao.find(isbn, name, minPrice, maxPrice, author, language);
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

    @Override
    public List<WishListItem> userWishList(String username) { return bookDao.userWishList(username); }

    @Override
    public void addToWishList(WishListItem wishListItem) { bookDao.addToWishList(wishListItem); }

    @Override
    public void removeFromWishList(WishListItem wishListItem) { bookDao.removeFromWishList(wishListItem); }

    @Override
    public void addDiscount(Discount discount) { bookDao.addDiscount(discount); }

    @Override
    public Discount checkIfDiscountAll() { return bookDao.checkIfDiscountAll(); }

    @Override
    public Discount checkIfDiscountSpecific(String isbn) { return bookDao.checkIfDiscountSpecific(isbn); }
}
