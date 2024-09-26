package ru.otus.spring.hw.services;

import ru.otus.spring.hw.dtos.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findCommentsByBookId(long id);

    Optional<CommentDto> findById(long id);

    CommentDto insert(long bookId, String content);

    CommentDto updateById(long id, String content);

    void deleteById(long id);
}
