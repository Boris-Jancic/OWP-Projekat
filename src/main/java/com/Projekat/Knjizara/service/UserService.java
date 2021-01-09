package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.User;

import java.util.List;

public interface UserService {

    User findOne(String username);

    List<User> findAll(String username);

    List<User> find(String username);

    void save(User user);

    void update(User user);

    void delete(String username);
}
