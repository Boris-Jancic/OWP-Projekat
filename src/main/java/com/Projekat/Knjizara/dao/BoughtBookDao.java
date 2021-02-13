package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Report;

import java.util.Date;
import java.util.List;

public interface BoughtBookDao {

    public BoughtBook findOne(String id);

    public List<BoughtBook> findBoughtBooksOnReceipt(String id);

    public List<BoughtBook> findAll();

    public List<Report> report(Date from, Date to);

    public void save(BoughtBook boughtBook);

    public void delete(String id);
}
