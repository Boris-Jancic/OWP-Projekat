package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Report {
    private String name;
    private String author;
    private float price;
    private int numOfCopies;
}
