package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.ReceiptDao;
import com.Projekat.Knjizara.models.Receipt;
import com.Projekat.Knjizara.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbReceiptService implements ReceiptService {

    @Autowired
    private ReceiptDao receiptDao;

    @Override
    public Receipt findOne(String id) {
        return receiptDao.findOne(id);
    }

    @Override
    public List<Receipt> findByUser(String username) {
        return receiptDao.findByUser(username);
    }

    @Override
    public List<Receipt> findAll() {
        return receiptDao.findAll();
    }

    @Override
    public void save(Receipt receipt) {
        receiptDao.save(receipt);
    }

    @Override
    public void delete(String id) {
        receiptDao.delete(id);
    }
}
