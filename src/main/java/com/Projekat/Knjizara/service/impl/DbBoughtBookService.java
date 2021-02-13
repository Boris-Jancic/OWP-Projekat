package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.BoughtBookDao;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Report;
import com.Projekat.Knjizara.service.BoughtBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DbBoughtBookService implements BoughtBookService {

    @Autowired
    private BoughtBookDao boughtBookDao;

    @Override
    public BoughtBook findOne(String id) {
        return boughtBookDao.findOne(id);
    }

    @Override
    public List<BoughtBook> findBoughtBooksOnReceipt(String id) {
        return boughtBookDao.findBoughtBooksOnReceipt(id);
    }

    @Override
    public List<BoughtBook> findAll() {
        return boughtBookDao.findAll();
    }

    @Override
    public List<Report> report(Date from, Date to) { return boughtBookDao.report(from, to); }

    @Override
    public void save(BoughtBook boughtBook) {
        boughtBookDao.save(boughtBook);
    }

    @Override
    public void delete(String id) {
        boughtBookDao.delete(id);
    }
}
