package ru.otus.spring.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.CommentDto;

public interface CommentService {
    Flux<CommentDto> findCommentsByBookId(long id);

    Mono<CommentDto> findById(long id);

    Mono<CommentDto> insert(long bookId, String content);

    Mono<CommentDto> updateById(long id, String content);

    Mono<Void> deleteById(long id);
}