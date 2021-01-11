package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.User;

import java.util.List;

public interface UserDao {

    public User findOne(String username);

    public User checkLogin(String username, String password);

    public List<User> findAll();

    public List<User> find(String username);

    public void save(User user);

    public void update(User user);

    public void delete(String username);
}
