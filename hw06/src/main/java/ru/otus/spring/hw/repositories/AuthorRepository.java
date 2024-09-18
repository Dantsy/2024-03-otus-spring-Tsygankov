package ru.otus.spring.hw.repositories;

import ru.otus.spring.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(long id);
}
