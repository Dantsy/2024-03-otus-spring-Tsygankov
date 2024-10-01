package ru.otus.spring.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.models.Author;

import java.util.List;

@ActiveProfiles("AuthorRepository test")
@DataMongoTest
@EnableMongock
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
    }

    @DisplayName("should save an author")
    @Test
    void saveAuthorTest() {
        var author = new Author("1", "John Doe");
        var savedAuthor = authorRepository.save(author);

        Assertions.assertThat(savedAuthor).isNotNull();
        Assertions.assertThat(savedAuthor.getId()).isNotNull();
        Assertions.assertThat(savedAuthor.getFullName()).isEqualTo(author.getFullName());
    }

    @DisplayName("should find an author by id")
    @Test
    void findAuthorByIdTest() {
        var author = new Author("1", "John Doe");
        var savedAuthor = authorRepository.save(author);

        var foundAuthor = authorRepository.findById(savedAuthor.getId());

        Assertions.assertThat(foundAuthor).isPresent();
        Assertions.assertThat(foundAuthor.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(author);
    }

    @DisplayName("should find all authors")
    @Test
    void findAllAuthorsTest() {
        var author1 = new Author("1", "John Doe");
        var author2 = new Author("2", "Jane Smith");

        authorRepository.saveAll(List.of(author1, author2));

        var allAuthors = authorRepository.findAll();

        Assertions.assertThat(allAuthors).hasSize(2);
        Assertions.assertThat(allAuthors).extracting("fullName").containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @DisplayName("should update an author")
    @Test
    void updateAuthorTest() {
        var author = new Author("1", "John Doe");
        var savedAuthor = authorRepository.save(author);

        savedAuthor.setFullName("Updated Name");
        authorRepository.save(savedAuthor);

        var updatedAuthor = authorRepository.findById(savedAuthor.getId());

        Assertions.assertThat(updatedAuthor).isPresent();
        Assertions.assertThat(updatedAuthor.get().getFullName()).isEqualTo("Updated Name");
    }

    @DisplayName("should delete an author by id")
    @Test
    void deleteAuthorByIdTest() {
        var author = new Author("1", "John Doe");
        var savedAuthor = authorRepository.save(author);

        authorRepository.deleteById(savedAuthor.getId());

        var deletedAuthor = authorRepository.findById(savedAuthor.getId());

        Assertions.assertThat(deletedAuthor).isNotPresent();
    }

    @DisplayName("should find an author by full name")
    @Test
    void findAuthorByFullNameTest() {
        var author = new Author("1", "John Doe");
        authorRepository.save(author);

        var foundAuthor = authorRepository.findByFullName("John Doe");

        Assertions.assertThat(foundAuthor).isPresent();
        Assertions.assertThat(foundAuthor.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(author);
    }

    @DisplayName("should return empty optional for non-existent author full name")
    @Test
    void findNonExistentAuthorByFullNameTest() {
        var nonExistentFullName = "Non Existent";
        var foundAuthor = authorRepository.findByFullName(nonExistentFullName);

        Assertions.assertThat(foundAuthor).isNotPresent();
    }
}
