package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Receipt {
    private List<BoughtBook> boughtBooks;
    private float finalPrice;
    private LocalDateTime dateOfPurchase;
    private User client;
    private int numberOfBooksBought;
}
