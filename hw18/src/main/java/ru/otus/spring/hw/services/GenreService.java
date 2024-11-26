package ru.otus.spring.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.spring.hw.dtos.GenreDto;

public interface GenreService {
    Flux<GenreDto> findAll();
}
