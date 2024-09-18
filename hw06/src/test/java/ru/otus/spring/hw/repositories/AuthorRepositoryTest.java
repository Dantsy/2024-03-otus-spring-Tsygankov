package ru.otus.spring.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.mappers.DtoMapperImpl;

@DisplayName("Jpa based repository for working with authors")
@DataJpaTest
@Import({AuthorRepositoryJpa.class, DtoMapperImpl.class})
@ActiveProfiles("test")
public class AuthorRepositoryTest {

    private static final int FIRST_AUTHOR_INDEX = 1;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DtoMapper mapper;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should get the author by id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthorDto = mapper.authorToAuthorDto(TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX));
        var actualAuthorDto = authorRepository.findById(expectedAuthorDto.getId()).map(mapper::authorToAuthorDto);
        Assertions.assertThat(actualAuthorDto).isPresent()
                .get().isEqualTo(expectedAuthorDto);
    }
}
