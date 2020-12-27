package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoyaltyCard {
    private float discount;
    private int points;
}
