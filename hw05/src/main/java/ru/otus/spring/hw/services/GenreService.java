package ru.otus.spring.hw.services;

import ru.otus.spring.hw.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
