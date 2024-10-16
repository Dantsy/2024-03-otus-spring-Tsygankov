package ru.otus.spring.hw.services;

import ru.otus.spring.hw.dtos.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
