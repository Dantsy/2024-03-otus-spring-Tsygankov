package ru.otus.spring.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment with id %d not found";
    private static final String BOOK_NOT_FOUND_MESSAGE = "Book with id %d not found";

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final DtoMapper mapper;

    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyCommentDtoList")
    public List<CommentDto> findCommentsByBookId(long id) {
        List<Comment> comments = commentRepository.findAllByBookId(id);
        return comments.stream()
                .map(mapper::commentToCommentDto)
                .toList();
    }

    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyComment")
    public CommentDto findById(long id) {
        return commentRepository.findById(id)
                .map(mapper::commentToCommentDto)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(id)));
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyComment")
    @Retry(name = "retryService")
    public CommentDto insert(long bookId, String content) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        Comment comment = new Comment(0, content, book);
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyComment")
    public CommentDto updateById(long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(id)));
        comment.setContent(content);
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    public CommentDto fallbackEmptyComment(Throwable e) {
        return new CommentDto();
    }

    public List<CommentDto> fallbackEmptyCommentDtoList(Throwable e) {
        return new ArrayList<>();
    }
}