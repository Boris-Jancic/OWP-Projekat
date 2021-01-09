package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.User;

import java.util.List;

public interface UserDao {

    public User findOne(String username);

    public List<User> findAll(String username);

    public List<User> find(String username);

    public void save(User user);

    public void update(User user);

    public void delete(String username);
}
