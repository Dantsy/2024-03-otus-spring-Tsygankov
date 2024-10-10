package ru.otus.spring.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.services.GenreService;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = GenreRestController.class)
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    private static final String GENRE_API_PATH = "/api/genres";

    @Test
    @DisplayName("Should return correct genre list")
    public void shouldReturnCorrectGenreList() {
        var genreDtoList = Flux.just(
                new GenreDto(1L, "Genre1"),
                new GenreDto(2L, "Genre2")
        );

        given(genreService.findAll()).willReturn(genreDtoList);

        webTestClient.get().uri(GENRE_API_PATH).exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(2)
                .value(genreDtos -> assertThat(genreDtos).containsExactly(
                        new GenreDto(1L, "Genre1"),
                        new GenreDto(2L, "Genre2")
                ));
    }
}
