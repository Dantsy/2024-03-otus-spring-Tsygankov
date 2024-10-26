package ru.otus.spring.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.models.documents.GenreDocument;
import ru.otus.spring.hw.models.entities.Genre;

@Component
public class GenreMapper {
    public GenreDocument toDocument(Genre genre) {
        return new GenreDocument(genre.getId().toString(), genre.getName());
    }
}