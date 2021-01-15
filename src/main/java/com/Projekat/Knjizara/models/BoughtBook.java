package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoughtBook {
    private String id;
    private String username;
    private Book book;
    private int numOfCopies;
    private float price; // generise aplikacija (book.price * numberOfCopies)
    private String receiptID;
}
