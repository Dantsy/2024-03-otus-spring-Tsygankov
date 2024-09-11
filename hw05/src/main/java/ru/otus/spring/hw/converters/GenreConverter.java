package ru.otus.spring.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
