package ru.otus.spring.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
