package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.BookRepositoryEnhanced;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Service for working with books")
@SpringBootTest
public class BookServiceTest {

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

    private List<Book> books;
    private List<BookDto> bookDtos;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
        books = TestDataHolder.getBooks();
        bookDtos = books.stream().map(book -> new BookDto(
                book.getId(),
                book.getTitle(),
                new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName()),
                book.getGenres().stream().map(genre -> new GenreDto(genre.getId(), genre.getName())).collect(Collectors.toList()),
                book.getComments().stream().map(comment -> new CommentDto(comment.getId(), comment.getContent())).collect(Collectors.toList())
        )).toList();
    }

    @Test
    @DisplayName("should find book by id with details")
    void shouldFindBookByIdWithDetails() {
        Book book = books.get(0);
        Mockito.when(bookRepositoryEnhanced.findByIdWithDetails(book.getId())).thenReturn(Mono.just(book));
        Mockito.when(mapper.bookToBookDTO(book)).thenReturn(bookDtos.get(0));

        Mono<BookDto> result = bookService.findByIdWithDetails(book.getId());

        StepVerifier.create(result)
                .expectNext(bookDtos.get(0))
                .verifyComplete();
    }

    @Test
    @DisplayName("should throw exception when book not found by id with details")
    void shouldThrowExceptionWhenBookNotFoundByIdWithDetails() {
        Mockito.when(bookRepositoryEnhanced.findByIdWithDetails(1L)).thenReturn(Mono.empty());

        Mono<BookDto> result = bookService.findByIdWithDetails(1L);

        StepVerifier.create(result)
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("should find all books with details")
    void shouldFindAllBooksWithDetails() {
        Mockito.when(bookRepositoryEnhanced.findAllWithDetails()).thenReturn(Flux.fromIterable(books));
        Mockito.when(mapper.bookToBookDTO(Mockito.any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return new BookDto(
                    book.getId(),
                    book.getTitle(),
                    new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName()),
                    book.getGenres().stream().map(genre -> new GenreDto(genre.getId(), genre.getName())).collect(Collectors.toList()),
                    book.getComments().stream().map(comment -> new CommentDto(comment.getId(), comment.getContent())).collect(Collectors.toList())
            );
        });

        Flux<BookDto> result = bookService.findAllWithDetails();

        StepVerifier.create(result)
                .expectNextMatches(bookDtos::contains)
                .expectNextMatches(bookDtos::contains)
                .expectNextMatches(bookDtos::contains)
                .verifyComplete();
    }

    @Test
    @DisplayName("should find book by id")
    void shouldFindBookById() {
        Book book = books.get(0);
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        Mockito.when(mapper.bookToBookDTO(book)).thenReturn(bookDtos.get(0));

        Mono<BookDto> result = bookService.findById(book.getId());

        StepVerifier.create(result)
                .expectNext(bookDtos.get(0))
                .verifyComplete();
    }

    @Test
    @DisplayName("should throw exception when book not found by id")
    void shouldThrowExceptionWhenBookNotFoundById() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<BookDto> result = bookService.findById(1L);

        StepVerifier.create(result)
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("should find all books")
    void shouldFindAllBooks() {
        Mockito.when(bookRepository.findAll()).thenReturn(Flux.fromIterable(books));
        Mockito.when(mapper.bookToBookDTO(Mockito.any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return new BookDto(
                    book.getId(),
                    book.getTitle(),
                    new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName()),
                    book.getGenres().stream().map(genre -> new GenreDto(genre.getId(), genre.getName())).collect(Collectors.toList()),
                    book.getComments().stream().map(comment -> new CommentDto(comment.getId(), comment.getContent())).collect(Collectors.toList())
            );
        });

        Flux<BookDto> result = bookService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(bookDtos::contains)
                .expectNextMatches(bookDtos::contains)
                .expectNextMatches(bookDtos::contains)
                .verifyComplete();
    }

    @Test
    @DisplayName("should delete book by id")
    void shouldDeleteBookById() {
        Mockito.when(bookRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = bookService.deleteById(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("should save book")
    void shouldSaveBook() {
        Book book = books.get(0);
        BookDto bookDto = bookDtos.get(0);
        Mockito.when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Mono.just(book.getAuthor()));
        Mockito.when(genreRepository.findAllByIdIn(Mockito.anyList())).thenReturn(Flux.fromIterable(book.getGenres()));
        Mockito.when(commentRepository.findAllByIdIn(Mockito.anyList())).thenReturn(Flux.fromIterable(book.getComments()));
        Mockito.when(bookRepositoryEnhanced.save(Mockito.any(Book.class))).thenReturn(Mono.just(book));
        Mockito.when(mapper.bookToBookDTO(book)).thenReturn(bookDto);

        Mono<BookDto> result = bookService.save(book.getId(), book.getTitle(), book.getAuthor().getId(),
                book.getGenres().stream().map(Genre::getId).toList(),
                book.getComments().stream().map(Comment::getId).toList());

        StepVerifier.create(result)
                .expectNext(bookDto)
                .verifyComplete();
    }
}