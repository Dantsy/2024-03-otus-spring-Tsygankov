package ru.otus.spring.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.models.Book;

import java.util.List;

@ActiveProfiles("BookRepository test")
@DataMongoTest
@EnableMongock
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @DisplayName("should save a book")
    @Test
    void saveBookTest() {
        var book = new Book("1", "Test Book", null, List.of("Genre1", "Genre2"), null);
        var savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(savedBook.getGenres()).isEqualTo(book.getGenres());
    }

    @DisplayName("should find a book by id")
    @Test
    void findBookByIdTest() {
        var book = new Book("1", "Test Book", null, List.of("Genre1", "Genre2"), null);
        var savedBook = bookRepository.save(book);

        var foundBook = bookRepository.findById(savedBook.getId());

        Assertions.assertThat(foundBook).isPresent();
        Assertions.assertThat(foundBook.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(book);
    }

    @DisplayName("should find all books")
    @Test
    void findAllBooksTest() {
        var book1 = new Book("1", "Test Book 1", null, List.of("Genre1"), null);
        var book2 = new Book("2", "Test Book 2", null, List.of("Genre2"), null);

        bookRepository.saveAll(List.of(book1, book2));

        var allBooks = bookRepository.findAll();

        Assertions.assertThat(allBooks).hasSize(2);
        Assertions.assertThat(allBooks).extracting("title").containsExactlyInAnyOrder("Test Book 1", "Test Book 2");
    }

    @DisplayName("should update a book")
    @Test
    void updateBookTest() {
        var book = new Book("1", "Test Book", null, List.of("Genre1"), null);
        var savedBook = bookRepository.save(book);

        savedBook.setTitle("Updated Title");
        savedBook.setGenres(List.of("Updated Genre"));
        bookRepository.save(savedBook);

        var updatedBook = bookRepository.findById(savedBook.getId());

        Assertions.assertThat(updatedBook).isPresent();
        Assertions.assertThat(updatedBook.get().getTitle()).isEqualTo("Updated Title");
        Assertions.assertThat(updatedBook.get().getGenres()).isEqualTo(List.of("Updated Genre"));
    }

    @DisplayName("should delete a book by id")
    @Test
    void deleteBookByIdTest() {
        var book = new Book("1", "Test Book", null, List.of("Genre1"), null);
        var savedBook = bookRepository.save(book);

        bookRepository.deleteById(savedBook.getId());

        var deletedBook = bookRepository.findById(savedBook.getId());

        Assertions.assertThat(deletedBook).isNotPresent();
    }

    @DisplayName("should return empty optional for non-existent book id")
    @Test
    void findNonExistentBookByIdTest() {
        var nonExistentId = "non-existent-id";
        var foundBook = bookRepository.findById(nonExistentId);

        Assertions.assertThat(foundBook).isNotPresent();
    }
}