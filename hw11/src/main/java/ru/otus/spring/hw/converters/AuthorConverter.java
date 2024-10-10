package ru.otus.spring.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.dtos.AuthorDto;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto authorDto) {
        if (authorDto == null) {
            return "Author is null";
        }
        return "Id: %d, FullName: %s".formatted(authorDto.getId(), authorDto.getFullName());
    }
}
