package ru.otus.spring.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.models.Genre;

import java.util.Arrays;
import java.util.List;

@DisplayName("Jpa based repository for working with genres")
@DataJpaTest
@Import({DtoMapperImpl.class})
@ActiveProfiles("test")
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should find genres by their IDs")
    @Test
    void shouldReturnCorrectGenresByIds() {
        List<Long> genreIds = Arrays.asList(1L, 2L);

        List<Genre> foundGenres = genreRepository.findAllByIdIn(genreIds);

        Assertions.assertThat(foundGenres).hasSize(2);
        Assertions.assertThat(foundGenres).extracting(Genre::getName)
                .containsExactlyInAnyOrder("Genre_1", "Genre_2");
    }

    @DisplayName("should return empty list if no genres found by IDs")
    @Test
    void shouldReturnEmptyListIfNoGenresFound() {
        List<Long> genreIds = Arrays.asList(999L, 1000L);

        List<Genre> foundGenres = genreRepository.findAllByIdIn(genreIds);

        Assertions.assertThat(foundGenres).isEmpty();
    }
}
