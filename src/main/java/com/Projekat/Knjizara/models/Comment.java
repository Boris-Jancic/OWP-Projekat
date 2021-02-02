package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String id;
    private String text;
    private float rating;
    private String dateOfComment;
    private User author;
    private Book book;
    private EStatus status;
}
