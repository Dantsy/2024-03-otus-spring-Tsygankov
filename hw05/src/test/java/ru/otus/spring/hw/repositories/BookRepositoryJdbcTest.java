package ru.otus.spring.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

@DisplayName("Jdbc-based repository for working with books")
@JdbcTest
@Import({BookRepositoryJdbc.class, GenreRepositoryJdbc.class, AuthorRepositoryJdbc.class, GenreRelationsRepositoryJdbc.class})
@ActiveProfiles("test")
class BookRepositoryJdbcTest {

    @Autowired
    private BookRepositoryJdbc repositoryJdbc;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Must load the book by id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repositoryJdbc.findById(expectedBook.getId());
        Assertions.assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Should load a list of all books")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAll();
        var expectedBooks = dbBooks;

        Assertions.assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("Must save a new book")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJdbc.save(expectedBook);
        Assertions.assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        Assertions.assertThat(repositoryJdbc.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Must save the modified workbook")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        Assertions.assertThat(repositoryJdbc.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJdbc.save(expectedBook);
        Assertions.assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        Assertions.assertThat(repositoryJdbc.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Must delete a book by id")
    @Test
    void shouldDeleteBook() {
        Assertions.assertThat(repositoryJdbc.findById(1L)).isPresent();
        repositoryJdbc.deleteById(1L);
        Assertions.assertThat(repositoryJdbc.findById(1L)).isEmpty();
    }

    @DisplayName("Should handle update of non-existing book")
    @Test
    void shouldHandleUpdateOfNonExistingBook() {
        long nonExistingBookId = 9999L;
        var nonExistingBook = new Book(nonExistingBookId, "NonExistingBookTitle", dbAuthors.get(0), List.of(dbGenres.get(0)));

        Assertions.assertThatThrownBy(() -> repositoryJdbc.save(nonExistingBook))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Should return empty Optional for non-existing book")
    @Test
    void shouldReturnEmptyOptionalForNonExistingBook() {
        long nonExistingBookId = 9999L;
        var actualBook = repositoryJdbc.findById(nonExistingBookId);
        Assertions.assertThat(actualBook).isEmpty();
    }

    @DisplayName("Should handle delete of non-existing book")
    @Test
    void shouldHandleDeleteOfNonExistingBook() {
        long nonExistingBookId = 9999L;

        repositoryJdbc.deleteById(nonExistingBookId);

        var allBooksAfterDelete = repositoryJdbc.findAll();
        Assertions.assertThat(allBooksAfterDelete).containsExactlyElementsOf(dbBooks);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}