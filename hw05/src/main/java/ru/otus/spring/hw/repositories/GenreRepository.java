package ru.otus.spring.hw.repositories;

import ru.otus.spring.hw.models.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();

    List<Genre> findAllByIds(List<Long> ids);
}
