package ru.otus.spring.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.Optional;
import java.util.stream.Collectors;

@DisplayName("Service for working with authors")
@SpringBootTest(classes = {AuthorServiceImpl.class, AuthorRepository.class, DtoMapperImpl.class})
@ActiveProfiles("test")
class AuthorServiceTest {

    private static final int FIRST_AUTHOR_INDEX = 0;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should return a list of all authors")
    @Test
    void findAll() {
        var expectedAuthors = TestDataHolder.getAuthors();
        Mockito.when(authorRepository.findAll()).thenReturn(expectedAuthors);

        var actualAuthorDtos = authorService.findAll();

        Assertions.assertThat(actualAuthorDtos).isNotNull()
                .hasSize(expectedAuthors.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedAuthors.stream().map(mapper::authorToAuthorDto).collect(Collectors.toList()));

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();
    }

    @DisplayName("should return author by id")
    @Test
    void findById() {
        var expectedAuthor = TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX);
        Mockito.when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedAuthor));

        var actualAuthorDto = authorService.findById(expectedAuthor.getId());

        Assertions.assertThat(actualAuthorDto).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mapper.authorToAuthorDto(expectedAuthor));

        Mockito.verify(authorRepository, Mockito.times(1)).findById(expectedAuthor.getId());
    }

    @DisplayName("must create a new author")
    @Test
    void insert() {
        var expectedAuthor = new Author(0, "New Author");
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenAnswer(invocation -> {
            Author sourceAuthor = invocation.getArgument(0);
            return new Author(1, sourceAuthor.getFullName());
        });

        var actualAuthorDto = authorService.insert(expectedAuthor.getFullName());

        Assertions.assertThat(actualAuthorDto).isNotNull()
                .matches(authorDto -> authorDto.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(mapper.authorToAuthorDto(expectedAuthor));

        Mockito.verify(authorRepository, Mockito.times(1)).save(Mockito.any(Author.class));
    }

    @DisplayName("should update the author")
    @Test
    void update() {
        var expectedAuthor = TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX);
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var actualAuthorDto = authorService.update(expectedAuthor.getId(), expectedAuthor.getFullName());

        Assertions.assertThat(actualAuthorDto).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mapper.authorToAuthorDto(expectedAuthor));

        Mockito.verify(authorRepository, Mockito.times(1)).save(Mockito.any(Author.class));
    }

    @DisplayName("should delete author by id")
    @Test
    void deleteById() {
        var expectedAuthor = TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX);

        authorService.deleteById(expectedAuthor.getId());

        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(expectedAuthor.getId());
    }
}