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
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.BookRepositoryEnhanced;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookRepositoryEnhanced bookRepositoryEnhanced;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private DtoMapper mapper;

    private Author author;
    private AuthorDto authorDto;
    private Genre genre1;
    private Genre genre2;
    private GenreDto genreDto1;
    private GenreDto genreDto2;
    private Comment comment1;
    private Comment comment2;
    private CommentDto commentDto1;
    private CommentDto commentDto2;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Author_1");
        authorDto = new AuthorDto(1L, "Author_1");
        genre1 = new Genre(1L, "Genre_1");
        genre2 = new Genre(2L, "Genre_2");
        genreDto1 = new GenreDto(1L, "Genre_1");
        genreDto2 = new GenreDto(2L, "Genre_2");
        comment1 = new Comment(1L, "Comment_1", 1L);
        comment2 = new Comment(2L, "Comment_2", 1L);
        commentDto1 = new CommentDto(1L, "Comment_1", 1L);
        commentDto2 = new CommentDto(2L, "Comment_2", 1L);
        book = new Book(1L, "Book_1", author, List.of(genre1, genre2), List.of(comment1, comment2));
        bookDto = new BookDto(1L, "Book_1", authorDto, List.of(genreDto1, genreDto2), List.of(commentDto1, commentDto2));
    }

    @Test
    @DisplayName("Should return book by id with details")
    public void shouldReturnBookByIdWithDetails() {
        given(bookRepositoryEnhanced.findByIdWithDetails(1L)).willReturn(Mono.just(book));
        given(mapper.bookToBookDTO(book)).willReturn(bookDto);

        StepVerifier.create(bookService.findByIdWithDetails(1L))
                .expectNext(bookDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when book not found by id with details")
    public void shouldThrowExceptionWhenBookNotFoundByIdWithDetails() {
        given(bookRepositoryEnhanced.findByIdWithDetails(1L)).willReturn(Mono.empty());

        StepVerifier.create(bookService.findByIdWithDetails(1L))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return all books with details")
    public void shouldReturnAllBooksWithDetails() {
        given(bookRepositoryEnhanced.findAllWithDetails()).willReturn(Flux.just(book));
        given(mapper.bookToBookDTO(book)).willReturn(bookDto);

        StepVerifier.create(bookService.findAllWithDetails())
                .expectNext(bookDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return book by id")
    public void shouldReturnBookById() {
        given(bookRepository.findById(1L)).willReturn(Mono.just(book));
        given(mapper.bookToBookDTO(book)).willReturn(bookDto);

        StepVerifier.create(bookService.findById(1L))
                .expectNext(bookDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when book not found by id")
    public void shouldThrowExceptionWhenBookNotFoundById() {
        given(bookRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(bookService.findById(1L))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return all books")
    public void shouldReturnAllBooks() {
        given(bookRepository.findAll()).willReturn(Flux.just(book));
        given(mapper.bookToBookDTO(book)).willReturn(bookDto);

        StepVerifier.create(bookService.findAll())
                .expectNext(bookDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should delete book by id")
    public void shouldDeleteBookById() {
        given(bookRepository.findById(1L)).willReturn(Mono.just(book));
        given(bookRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(bookService.deleteById(1L))
                .verifyComplete();

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should save new book")
    public void shouldSaveNewBook() {
        given(authorRepository.findById(1L)).willReturn(Mono.just(author));
        given(genreRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(Flux.just(genre1, genre2));
        given(commentRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(Flux.just(comment1, comment2));
        given(bookRepositoryEnhanced.save(any(Book.class))).willReturn(Mono.just(book));
        given(mapper.bookToBookDTO(book)).willReturn(bookDto);

        StepVerifier.create(bookService.save(0, "Book_1", 1L, List.of(1L, 2L), List.of(1L, 2L)))
                .expectNext(bookDto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when saving book with non-existing comments")
    public void shouldThrowExceptionWhenSavingBookWithNonExistingComments() {
        given(authorRepository.findById(1L)).willReturn(Mono.just(author));
        given(genreRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(Flux.just(genre1, genre2));
        given(commentRepository.findAllByIdIn(List.of(1L, 2L))).willReturn(Flux.empty());

        StepVerifier.create(bookService.save(0, "Book_1", 1L, List.of(1L, 2L), List.of(1L, 2L)))
                .expectError(EntityNotFoundException.class)
                .verify();
    }
}