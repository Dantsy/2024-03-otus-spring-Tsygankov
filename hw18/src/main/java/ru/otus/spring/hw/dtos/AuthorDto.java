package ru.otus.spring.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private long id;

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

}
