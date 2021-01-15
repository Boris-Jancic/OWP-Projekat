package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.BoughtBook;

import java.util.List;

public interface BoughtBookDao {

    public BoughtBook findOne(String id);

    public List<BoughtBook> findBoughtBooksOnReceipt(String id);

    public List<BoughtBook> findAll();

    public void save(BoughtBook boughtBook);

    public void delete(String id);
}
