package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private DtoMapper mapper;

    private Comment comment;
    private CommentDto commentDto;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Author_1");
        genre = new Genre(1L, "Genre_1");
        comment = new Comment(1L, "Comment_1", 1L);
        book = new Book(1L, "Book_1", author, List.of(genre), List.of(comment));
        commentDto = new CommentDto(1L, "Comment_1", book.getId());
    }

    @Test
    @DisplayName("Should return comments by book id")
    public void shouldReturnCommentsByBookId() {
        given(commentRepository.findAllByBookId(1L)).willReturn(Flux.just(comment));
        given(mapper.commentToCommentDto(comment)).willReturn(commentDto);

        StepVerifier.create(commentService.findCommentsByBookId(1L))
                .expectNext(commentDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return comment by id")
    public void shouldReturnCommentById() {
        given(commentRepository.findById(1L)).willReturn(Mono.just(comment));
        given(mapper.commentToCommentDto(comment)).willReturn(commentDto);

        StepVerifier.create(commentService.findById(1L))
                .expectNext(commentDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when comment not found by id")
    public void shouldThrowExceptionWhenCommentNotFoundById() {
        given(commentRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(commentService.findById(1L))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should insert new comment")
    public void shouldInsertNewComment() {
        given(bookRepository.findById(1L)).willReturn(Mono.just(book));
        given(commentRepository.save(any(Comment.class))).willReturn(Mono.just(comment));
        given(mapper.commentToCommentDto(comment)).willReturn(commentDto);

        StepVerifier.create(commentService.insert(1L, "Comment_1"))
                .expectNext(commentDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when inserting comment with non-existing book")
    public void shouldThrowExceptionWhenInsertingCommentWithNonExistingBook() {
        given(bookRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(commentService.insert(1L, "Comment_1"))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update comment by id")
    public void shouldUpdateCommentById() {
        given(commentRepository.findById(1L)).willReturn(Mono.just(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(Mono.just(comment));
        given(mapper.commentToCommentDto(comment)).willReturn(commentDto);

        StepVerifier.create(commentService.updateById(1L, "Updated_Comment_1"))
                .expectNext(commentDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing comment")
    public void shouldThrowExceptionWhenUpdatingNonExistingComment() {
        given(commentRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(commentService.updateById(1L, "Updated_Comment_1"))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should delete comment by id")
    public void shouldDeleteCommentById() {
        given(commentRepository.findById(1L)).willReturn(Mono.just(comment));
        given(commentRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(commentService.deleteById(1L))
                .verifyComplete();

        verify(commentRepository, times(1)).deleteById(1L);
    }

}