package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String id;

    @NotBlank
    private String text;

    @Min(1)
    @Max(5)
    private int rating;

    private String dateOfComment;
    private User author;
    private Book book;
    private EStatus status;
}
