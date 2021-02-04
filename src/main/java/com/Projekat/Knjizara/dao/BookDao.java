package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Discount;
import com.Projekat.Knjizara.models.WishListItem;

import java.util.Date;
import java.util.List;

public interface BookDao {

    public Book findOne(String isbn);

    public List<Book> findAll();

    public List<Book> find(String isbn, String name, float minPrice, float maxPrice, String author, String language);

    public void save(Book book);

    public void update(Book book);

    public void delete(String isbn);

    public List<WishListItem> userWishList(String username);

    public void addToWishList(WishListItem wishListItem);

    public void removeFromWishList(WishListItem wishListItem);

    public void addDiscount(Discount discount);

    public Discount checkIfDiscountAll();

    public Discount checkIfDiscountSpecific(String isbn);
}
