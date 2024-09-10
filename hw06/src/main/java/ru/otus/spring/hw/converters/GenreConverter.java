package ru.otus.spring.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.dtos.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genreDto) {
        return "Id: %d, Name: %s".formatted(genreDto.getId(), genreDto.getName());

    }
}
