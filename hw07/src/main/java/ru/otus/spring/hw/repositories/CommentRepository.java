package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBookId(long bookId);
}