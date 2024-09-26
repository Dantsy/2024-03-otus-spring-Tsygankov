package ru.otus.spring.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Genre;

import java.util.List;

@DisplayName("Jpa based repository for working with books")
@DataJpaTest
@Import({DtoMapperImpl.class})
@ActiveProfiles("test")
class BookRepositoryJpaTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DtoMapper mapper;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("must load the book by id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        Assertions.assertThat(actualBook).isPresent()
                .map(book -> mapper.bookToBookDTO(book))
                .get().usingRecursiveComparison()
                .isEqualTo(mapper.bookToBookDTO(expectedBook));
    }

    @DisplayName("should load a list of all books")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll().stream().map(mapper::bookToBookDTO).toList();
        var expectedBooks = TestDataHolder.getBooks().stream().map(mapper::bookToBookDTO).toList();

        Assertions.assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("must save a new book")
    @Test
    void shouldSaveNewBook() {
        Author author = entityManager.find(Author.class, TestDataHolder.getAuthors().get(2).getId());
        List<Genre> genres = List.of(
                entityManager.find(Genre.class, TestDataHolder.getGenres().get(0).getId()),
                entityManager.find(Genre.class, TestDataHolder.getGenres().get(2).getId()));

        var expectedBook = new Book(0, "BookTitle_10500", author, genres);
        var expectedBookDto = mapper.bookToBookDTO(expectedBook);
        var returnedBook = bookRepository.save(expectedBook);
        var returnedBookDto = mapper.bookToBookDTO(returnedBook);

        Assertions.assertThat(returnedBookDto).isNotNull()
                .matches(bookDto -> bookDto.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(expectedBookDto);

        Assertions.assertThat(mapper.bookToBookDTO(entityManager.find(Book.class, returnedBook.getId())))
                .isEqualTo(returnedBookDto);
    }

    @DisplayName("must save the modified workbook")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", TestDataHolder.getAuthors().get(2),
                List.of(TestDataHolder.getGenres().get(4), TestDataHolder.getGenres().get(5)));
        var expectedBookDto = mapper.bookToBookDTO(expectedBook);
        Assertions.assertThat(entityManager.find(Book.class, expectedBook.getId()))
                .isNotEqualTo(expectedBook);

        var returnedBookDto = mapper.bookToBookDTO(bookRepository.save(expectedBook));
        Assertions.assertThat(returnedBookDto).isNotNull()
                .matches(bookDto -> bookDto.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBookDto);

        Assertions.assertThat(mapper.bookToBookDTO(entityManager.find(Book.class, returnedBookDto.getId())))
                .isEqualTo(returnedBookDto);
    }

    @DisplayName("must delete a book by id")
    @Test
    void shouldDeleteBook() {
        Assertions.assertThat(entityManager.find(Book.class, FIRST_BOOK_ID)).isNotNull();
        bookRepository.deleteById(FIRST_BOOK_ID);
        Assertions.assertThat(entityManager.find(Book.class, FIRST_BOOK_ID)).isNull();
    }

    @DisplayName("should handle update of non-existing book")
    @Test
    void shouldHandleUpdateOfNonExistingBook() {
        var nonExistingBook = new Book(9999L, "NonExistingBookTitle", TestDataHolder.getAuthors().get(0),
                List.of(TestDataHolder.getGenres().get(0)));

        var updatedBook = bookRepository.save(nonExistingBook);

        Assertions.assertThat(updatedBook).isNotNull()
                .matches(book -> book.getId() != 9999L);

        Assertions.assertThat(entityManager.find(Book.class, updatedBook.getId()))
                .isEqualTo(updatedBook);
    }

    @DisplayName("should return empty Optional for non-existing book")
    @Test
    void shouldReturnEmptyOptionalForNonExistingBook() {
        var nonExistingBook = bookRepository.findById(9999L);

        Assertions.assertThat(nonExistingBook).isNotPresent();
    }

    @DisplayName("should handle delete of non-existing book")
    @Test
    void shouldHandleDeleteOfNonExistingBook() {
        bookRepository.deleteById(9999L);

        Assertions.assertThat(bookRepository.findById(9999L)).isEmpty();
    }

    public static List<Book> getBooks() {
        return TestDataHolder.getBooks();
    }
}