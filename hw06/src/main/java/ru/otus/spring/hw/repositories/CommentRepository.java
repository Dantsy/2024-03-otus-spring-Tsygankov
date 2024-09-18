package ru.otus.spring.hw.repositories;

import ru.otus.spring.hw.models.Comment;

import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}
