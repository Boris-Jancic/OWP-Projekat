package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.UserDao;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserService implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findOne(String username) {
        return userDao.findOne(username);
    }

    @Override
    public User checkLogin(String username, String password) {
        return userDao.checkLogin(username, password);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> find(String username) {
        return userDao.find(username);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void block(String username, boolean blocked) {
        userDao.block(username, blocked);
    }
}
