package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Receipt {
    private String id;
    private List<BoughtBook> boughtBooks;
    private float finalPrice;
    private Date dateOfPurchase;
    private User client;
    private int numberOfBooksBought;
}
