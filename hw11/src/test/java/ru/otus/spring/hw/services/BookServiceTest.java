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
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.Optional;
import java.util.stream.Collectors;

@DisplayName("Service for working with books")
@SpringBootTest(classes = {BookServiceImpl.class, AuthorRepository.class, GenreRepository.class, CommentRepository.class, DtoMapperImpl.class})
@ActiveProfiles("test")
class BookServiceTest {

    private static final int FIRST_BOOK_INDEX = 0;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should return a completed book with the specified id")
    @Test
    void insert() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var authorId = expectedBook.getAuthor().getId();
        var genreIds = expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        var commentIds = expectedBook.getComments().stream().map(Comment::getId).collect(Collectors.toList());

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(expectedBook.getAuthor()));
        Mockito.when(genreRepository.findAllByIdIn(genreIds)).thenReturn(expectedBook.getGenres());
        Mockito.when(commentRepository.findAllByIdIn(commentIds)).thenReturn(expectedBook.getComments());
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenAnswer(invocation -> {
            Book sourceBook = invocation.getArgument(0);
            return new Book(1, sourceBook.getTitle(), sourceBook.getAuthor(), sourceBook.getGenres(), sourceBook.getComments());
        });

        var actualBookDto = bookService.insert(expectedBook.getTitle(), authorId, genreIds, commentIds);

        Assertions.assertThat(actualBookDto).isNotNull()
                .matches(bookDto -> bookDto.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(mapper.bookToBookDTO(expectedBook));
    }
}