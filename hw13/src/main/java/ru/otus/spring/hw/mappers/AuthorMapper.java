package ru.otus.spring.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.models.documents.AuthorDocument;
import ru.otus.spring.hw.models.entities.Author;

@Component
public class AuthorMapper {
    public AuthorDocument toDocument(Author author) {
        return new AuthorDocument(author.getId().toString(), author.getFullName());
    }
}