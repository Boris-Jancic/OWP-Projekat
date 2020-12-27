package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor ;

import java.time.Year;

@Data
@NoArgsConstructor
public class Book {
    private String name;
    private String isbn; // Primary key
    private String authorHouse;
    private String Authors; // TODO: Promeni da budu autori a ne string
    private Year yearOfRelease;
    private String description;
    private float price;
    private int numOfPages;
    private String typeOfCover;
    private String letter;
    private String language;
    private float rating; // Based of user score
}
