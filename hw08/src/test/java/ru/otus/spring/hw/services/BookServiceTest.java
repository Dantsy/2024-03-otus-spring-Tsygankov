package ru.otus.spring.hw.services;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.List;

@ActiveProfiles("Service for working with books")
@Import(BookServiceImpl.class)
@DataMongoTest
@EnableMongock
class BookServiceTest {

    private static final int FIRST_BOOK_INDEX = 0;
    private static final String NON_EXISTENT_BOOK_ID = "non-existent-id";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
        authorRepository.saveAll(TestDataHolder.getAuthors());
        bookRepository.saveAll(TestDataHolder.getBooks());
    }

    @DisplayName("must return the book by id")
    @Test
    void findBookByIdTest() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var savedBook = bookRepository.save(expectedBook);

        var actualBook = bookService.findById(savedBook.getId());

        Assertions.assertThat(actualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);
    }

    @DisplayName("must return empty optional for non-existent book id")
    @Test
    void findNonExistentBookByIdTest() {
        var actualBook = bookService.findById(NON_EXISTENT_BOOK_ID);

        Assertions.assertThat(actualBook).isNotPresent();
    }

    @DisplayName("should return a completed book with the specified id")
    @Test
    void insert() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var author = expectedBook.getAuthor();

        var actualBookDto = bookService.insert(expectedBook.getTitle(), author.getId(), expectedBook.getGenres());

        Assertions.assertThat(actualBookDto)
                .isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);
    }

    @DisplayName("should update the book with the specified id")
    @Test
    void update() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var author = expectedBook.getAuthor();
        var updatedTitle = "Updated Title";
        var updatedGenres = List.of("Updated Genre");

        var savedBook = bookRepository.save(expectedBook);

        var updatedBook = bookService.update(savedBook.getId(), updatedTitle, author.getId(), updatedGenres);

        Assertions.assertThat(updatedBook)
                .isNotNull()
                .matches(book -> book.getTitle().equals(updatedTitle))
                .matches(book -> book.getGenres().equals(updatedGenres))
                .usingRecursiveComparison()
                .ignoringFields("title", "genres")
                .isEqualTo(new Book(savedBook.getId(), expectedBook.getTitle(), expectedBook.getAuthor(), expectedBook.getGenres(), expectedBook.getComments()));
    }

    @DisplayName("should return null when updating a non-existent book")
    @Test
    void updateNonExistentBookTest() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var author = expectedBook.getAuthor();
        var updatedTitle = "Updated Title";
        var updatedGenres = List.of("Updated Genre");

        var updatedBook = bookService.update(NON_EXISTENT_BOOK_ID, updatedTitle, author.getId(), updatedGenres);

        Assertions.assertThat(updatedBook).isNull();
    }

    @DisplayName("should delete the book with the specified id")
    @Test
    void deleteById() {
        var expectedBook = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var savedBook = bookRepository.save(expectedBook);

        bookService.deleteById(savedBook.getId());

        var deletedBook = bookRepository.findById(savedBook.getId());
        Assertions.assertThat(deletedBook).isNotPresent();
    }

    @DisplayName("should not throw an exception when deleting a non-existent book")
    @Test
    void deleteNonExistentBookTest() {
        Assertions.assertThatCode(() -> bookService.deleteById(NON_EXISTENT_BOOK_ID))
                .doesNotThrowAnyException();
    }
}