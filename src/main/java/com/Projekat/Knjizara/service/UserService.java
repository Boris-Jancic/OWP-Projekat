package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.User;

import java.util.List;

public interface UserService {

    User findOne(String username);

    User checkLogin(String username, String password);

    List<User> findAll();

    public List<User> loyaltyCardReq();

    public void updateLoyaltyCardReq(String request, String username);

    List<User> find(String username);

    void save(User user);

    void update(User user);

    void block(String username, boolean blocked);
}
