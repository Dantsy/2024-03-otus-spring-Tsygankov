package ru.otus.spring.hw.services;

import ru.otus.spring.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(String id);

    List<Book> findAll();

    Book insert(String title, String authorId, List<String> genresIds);

    Book update(String id, String title, String authorId, List<String> genresIds);

    void deleteById(String id);
}
