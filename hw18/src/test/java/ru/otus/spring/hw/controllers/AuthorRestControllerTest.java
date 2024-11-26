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
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.services.AuthorService;

import java.util.List;

@DisplayName("Controller for working with authors")
@WebFluxTest(AuthorRestController.class)
@Import({DtoMapperImpl.class})
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        authorDto = new AuthorDto(1L, "John Doe");
    }

    @Test
    @DisplayName("should return all authors")
    void shouldReturnAllAuthors() {
        Mockito.when(authorService.findAll()).thenReturn(Flux.just(authorDto));

        webTestClient.get().uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(1)
                .contains(authorDto);
    }

    @Test
    @DisplayName("should return author by id")
    void shouldReturnAuthorById() {
        Mockito.when(authorService.findById(1L)).thenReturn(Mono.just(authorDto));

        webTestClient.get().uri("/api/authors/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorDto);
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void shouldReturn404WhenAuthorNotFound() {
        Mockito.when(authorService.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/authors/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("should save author")
    void shouldSaveAuthor() {
        Mockito.when(authorService.update(1L, "John Doe")).thenReturn(Mono.just(authorDto));

        webTestClient.post().uri("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorDto);
    }

    @Test
    @DisplayName("should delete author")
    void shouldDeleteAuthor() {
        Mockito.when(authorService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/authors/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}