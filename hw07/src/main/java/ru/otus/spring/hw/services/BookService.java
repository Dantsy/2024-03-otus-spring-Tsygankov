package ru.otus.spring.hw.services;

import ru.otus.spring.hw.dtos.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, List<Long> genresIds);

    BookDto update(long id, String title, long authorId, List<Long> genresIds);

    void deleteById(long id);
}
