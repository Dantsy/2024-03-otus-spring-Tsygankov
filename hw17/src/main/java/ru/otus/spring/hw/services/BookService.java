package ru.otus.spring.hw.services;

import ru.otus.spring.hw.dtos.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, List<Long> genresIds, List<Long> commentIds);

    BookDto update(long id, String title, long authorId, List<Long> genresIds, List<Long> commentIds);

    void deleteById(long id);
}
