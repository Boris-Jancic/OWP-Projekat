package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.BoughtBookDao;
import com.Projekat.Knjizara.dao.impl.BoughtBookDaoImpl;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.service.BoughtBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DbBoughtBookService implements BoughtBookService {

    @Autowired
    private BoughtBookDao boughtBookDao;

    @Override
    public String generateRandID() {
        Random r = new Random();
        int randomID = r.nextInt(999999);
        return Integer.toString(randomID);
    }

    @Override
    public BoughtBook findOne(String id) {
        return boughtBookDao.findOne(id);
    }

    @Override
    public List<BoughtBook> findAll() {
        return boughtBookDao.findAll();
    }

    @Override
    public void save(BoughtBook boughtBook) {
        boughtBookDao.save(boughtBook);
    }

    @Override
    public void delete(String id) {
        boughtBookDao.delete(id);
    }
}
