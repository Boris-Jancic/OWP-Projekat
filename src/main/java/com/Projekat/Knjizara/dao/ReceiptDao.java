package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.Receipt;

import java.util.List;

public interface ReceiptDao {

    public Receipt findOne(String id);

    public List<Receipt> findByUser(String username);

    public List<Receipt> findAll();

    public void save(Receipt receipt);

    public void delete(String id);
}
