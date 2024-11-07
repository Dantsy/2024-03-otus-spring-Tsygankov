package ru.otus.spring.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @NonNull
    List<Comment> findAllByBookId(long id);

    List<Comment> findAllByIdIn(List<Long> ids);

}