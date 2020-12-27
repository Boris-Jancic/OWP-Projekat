package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private String commentText;
    private int rating;
    private LocalDateTime dateOfComment;
    private User commentCreator;
    private Book commentedBook;
    private Status status;
}
