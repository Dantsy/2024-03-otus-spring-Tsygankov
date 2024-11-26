package ru.otus.spring.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.services.GenreService;

@DisplayName("Controller for working with genres")
@WebFluxTest(GenreRestController.class)
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    private GenreDto genreDto;

    @BeforeEach
    void setUp() {
        genreDto = new GenreDto(1L, "Fiction");
    }

    @Test
    @DisplayName("should return all genres")
    void shouldReturnAllGenres() {
        Mockito.when(genreService.findAll()).thenReturn(Flux.just(genreDto));

        webTestClient.get().uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(1)
                .contains(genreDto);
    }
}