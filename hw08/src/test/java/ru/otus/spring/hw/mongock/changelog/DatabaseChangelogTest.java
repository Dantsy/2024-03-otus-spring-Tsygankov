package ru.otus.spring.hw.mongock.changelog;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.BookstoreApp;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("DatabaseChangelog test")
@ContextConfiguration(classes = {BookstoreApp.class, DatabaseChangelog.class})
@DataMongoTest
@EnableMongock
public class DatabaseChangelogTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testDatabaseChangelog() {

        List<Author> authors = authorRepository.findAll();
        List<Book> books = bookRepository.findAll();

        assertThat(authors).hasSize(8);
        assertThat(books).hasSize(10);

        assertThat(authors).anyMatch(author -> "George Orwell".equals(author.getFullName()));
        assertThat(authors).anyMatch(author -> "Jane Austen".equals(author.getFullName()));
        assertThat(authors).anyMatch(author -> "J.K. Rowling".equals(author.getFullName()));

        assertThat(books).anyMatch(book -> "1984".equals(book.getTitle()));
        assertThat(books).anyMatch(book -> "Pride and Prejudice".equals(book.getTitle()));
        assertThat(books).anyMatch(book -> "Harry Potter and the Philosopher's Stone".equals(book.getTitle()));
    }
}