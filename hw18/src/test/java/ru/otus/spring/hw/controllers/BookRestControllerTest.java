package ru.otus.spring.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;

import java.util.List;

@DisplayName("Controller for working with books")
@WebFluxTest(BookRestController.class)
@Import({DtoMapperImpl.class})
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    private BookDtoIds bookDtoIds;
    private BookDto bookDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        bookDtoIds = new BookDtoIds(1L, "Book Title", 1L, List.of(1L, 2L), List.of(1L, 2L));
        bookDto = new BookDto(1L, "Book Title", null, null, null);
        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("New Comment");
    }

    @Test
    @DisplayName("should return all books")
    void shouldReturnAllBooks() {
        Mockito.when(bookService.findAllWithDetails()).thenReturn(Flux.just(bookDto));

        webTestClient.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1)
                .contains(bookDto);
    }

    @Test
    @DisplayName("should return book by id")
    void shouldReturnBookById() {
        Mockito.when(bookService.findByIdWithDetails(1L)).thenReturn(Mono.just(bookDto));

        webTestClient.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    @DisplayName("should return 404 when book not found")
    void shouldReturn404WhenBookNotFound() {
        Mockito.when(bookService.findByIdWithDetails(1L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("should save book")
    void shouldSaveBook() {
        Mockito.when(bookService.save(1L, "Book Title", 1L, List.of(1L, 2L), List.of(1L, 2L))).thenReturn(Mono.just(bookDto));
        Mockito.when(commentService.insert(1L, "New Comment")).thenReturn(Mono.just(commentDto));

        webTestClient.post().uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDtoIds)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    @DisplayName("should delete book")
    void shouldDeleteBook() {
        Mockito.when(bookService.findById(1L)).thenReturn(Mono.just(bookDto));
        Mockito.when(bookService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/books/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}