package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

import static ru.otus.spring.hw.repositories.CommentRepository.COMMENT_NOT_FOUND_MESSAGE;
import static ru.otus.spring.hw.services.BookService.BOOK_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final DtoMapper mapper;

    @Override
    public Flux<CommentDto> findCommentsByBookId(long id) {
        return commentRepository.findAllByBookId(id)
                .map(mapper::commentToCommentDto);
    }

    @Override
    public Mono<CommentDto> findById(long id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(id))))
                .map(mapper::commentToCommentDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> insert(long bookId, String content) {
        return Mono.defer(() -> bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId))))
                .map(book -> new Comment(0, content, book.getId()))
                .flatMap(commentRepository::save)
                .map(mapper::commentToCommentDto));
    }

    @Override
    public Mono<CommentDto> updateById(long id, String content) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(id))))
                .doOnNext(comment -> comment.setContent(content))
                .flatMap(commentRepository::save)
                .map(mapper::commentToCommentDto);
    }

    @Override
    public Mono<Void> deleteById(long id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(id))))
                .then(commentRepository.deleteById(id))
                .then();
    }
}
