package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoughtBook {
    private Book book;
    private int numberOfCopies;
    private float price; // generise aplikacija (book.price * numberOfCopies)
}
