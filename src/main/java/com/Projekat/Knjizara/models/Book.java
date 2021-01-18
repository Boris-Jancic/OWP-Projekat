package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor ;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String name;

    @NotBlank(message = "Mora biti 13 cifara dugacak broj")
    @Size(max=13)
    private String isbn; // Primary key

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=25)
    private String publisher;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=40)
    private String author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date yearOfRelease;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=100)
    private String description;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=200)
    private String picture;

    @Min(1)
    private float price;

    @Min(1)
    private int numOfPages;

    private ECover typeOfCover;

    private ELetter letter;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=20)
    private String language;

    @Min(0)
    private int remaining;

    @Min(0)
    private float rating; // Based of user score

    private boolean active;
}

