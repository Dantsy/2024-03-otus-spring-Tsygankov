package ru.otus.spring.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping(path = "/api/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreService.findAll();
    }
}