package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import lombok.Data;
import lombok.NoArgsConstructor ;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Book {
    private String name;
    private String isbn; // Primary key
    private String publisher;
    private String authors;
    private Date yearOfRelease;
    private String description;
    private String picture;
    private float price;
    private int numOfPages;
    private ECover typeOfCover;
    private ELetter letter;
    private String language;
    private int remaining;
    private float rating; // Based of user score
    private boolean active;
}

