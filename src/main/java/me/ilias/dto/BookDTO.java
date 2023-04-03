package me.ilias.dto;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class BookDTO {
    @NotBlank
    private String title;
    @Min(10)
    private int pages;
    @Past
    private LocalDate publishDate;
    @NotNull
    private AuthorDTO author;
    @NotNull
    private String summary;
}
