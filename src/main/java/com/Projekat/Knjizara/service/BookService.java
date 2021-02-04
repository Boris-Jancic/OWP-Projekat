package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Discount;
import com.Projekat.Knjizara.models.WishListItem;

import java.util.List;

public interface BookService {

    Book findOne(String isbn);

    List<Book> findAll();

    List<Book> find(String isbn, String name, float minPrice, float maxPrice, String author, String language);

    void save(Book user);

    void update(Book user);

    void delete(String isbn);

    public List<WishListItem> userWishList(String username);

    public void addToWishList(WishListItem wishListItem);

    public void removeFromWishList(WishListItem wishListItem);

    public void addDiscount(Discount discount);

    public Discount checkIfDiscountAll();

    public Discount checkIfDiscountSpecific(String isbn);
}
