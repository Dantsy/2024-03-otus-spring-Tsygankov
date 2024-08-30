package ru.otus.spring.hw.services;

import ru.otus.spring.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
