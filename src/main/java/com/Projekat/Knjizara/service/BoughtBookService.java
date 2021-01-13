package com.Projekat.Knjizara.service;

import com.Projekat.Knjizara.models.BoughtBook;

import java.util.List;

public interface BoughtBookService {

    public String generateRandID();

    public BoughtBook findOne(String id);

    public List<BoughtBook> findAll();

    public void save(BoughtBook book);

    public void delete(String id);
}
