package ru.otus.spring.hw.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoIds {

    private long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Min(value = 1, message = "Author ID must be greater than 0")
    private long authorId;

    @NotEmpty(message = "Genre IDs cannot be empty")
    private List<Long> genreIds;

    private List<Long> commentIds;

}