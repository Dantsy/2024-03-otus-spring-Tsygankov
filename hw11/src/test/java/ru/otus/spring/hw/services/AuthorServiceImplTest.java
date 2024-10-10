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
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private DtoMapper mapper;

    private Author author1;
    private Author author2;
    private AuthorDto authorDto1;
    private AuthorDto authorDto2;

    @BeforeEach
    void setUp() {
        author1 = new Author(1L, "Author_1");
        author2 = new Author(2L, "Author_2");
        authorDto1 = new AuthorDto(1L, "Author_1");
        authorDto2 = new AuthorDto(2L, "Author_2");
    }

    @Test
    @DisplayName("Should return all authors")
    public void shouldReturnAllAuthors() {
        given(authorRepository.findAll()).willReturn(Flux.just(author1, author2));
        given(mapper.authorToAuthorDto(author1)).willReturn(authorDto1);
        given(mapper.authorToAuthorDto(author2)).willReturn(authorDto2);

        StepVerifier.create(authorService.findAll())
                .expectNext(authorDto1)
                .expectNext(authorDto2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return author by id")
    public void shouldReturnAuthorById() {
        given(authorRepository.findById(1L)).willReturn(Mono.just(author1));
        given(mapper.authorToAuthorDto(author1)).willReturn(authorDto1);

        StepVerifier.create(authorService.findById(1L))
                .expectNext(authorDto1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when author not found by id")
    public void shouldThrowExceptionWhenAuthorNotFoundById() {
        given(authorRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(authorService.findById(1L))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should insert new author")
    public void shouldInsertNewAuthor() {
        given(authorRepository.save(any(Author.class))).willReturn(Mono.just(author1));
        given(mapper.authorToAuthorDto(author1)).willReturn(authorDto1);

        StepVerifier.create(authorService.insert("Author_1"))
                .expectNext(authorDto1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update existing author")
    public void shouldUpdateExistingAuthor() {
        given(authorRepository.save(any(Author.class))).willReturn(Mono.just(author1));
        given(mapper.authorToAuthorDto(author1)).willReturn(authorDto1);

        StepVerifier.create(authorService.update(1L, "Author_1_Updated"))
                .expectNext(authorDto1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should delete author by id")
    public void shouldDeleteAuthorById() {
        given(authorRepository.findById(1L)).willReturn(Mono.just(author1));
        given(authorRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(authorService.deleteById(1L))
                .verifyComplete();

        verify(authorRepository, times(1)).deleteById(1L);
    }
}