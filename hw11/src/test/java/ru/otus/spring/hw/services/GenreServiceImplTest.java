package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private DtoMapper mapper;

    private Genre genre1;
    private Genre genre2;
    private GenreDto genreDto1;
    private GenreDto genreDto2;

    @BeforeEach
    void setUp() {
        genre1 = new Genre(1L, "Genre_1");
        genre2 = new Genre(2L, "Genre_2");
        genreDto1 = new GenreDto(1L, "Genre_1");
        genreDto2 = new GenreDto(2L, "Genre_2");
    }

    @Test
    @DisplayName("Should return all genres")
    public void shouldReturnAllGenres() {
        given(genreRepository.findAll()).willReturn(Flux.just(genre1, genre2));
        given(mapper.genreToGenreDto(genre1)).willReturn(genreDto1);
        given(mapper.genreToGenreDto(genre2)).willReturn(genreDto2);

        StepVerifier.create(genreService.findAll())
                .expectNext(genreDto1)
                .expectNext(genreDto2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle error when finding genres")
    public void shouldHandleErrorWhenFindingGenres() {
        given(genreRepository.findAll()).willReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(genreService.findAll())
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().contains("Error finding genres"))
                .verify();
    }
}
