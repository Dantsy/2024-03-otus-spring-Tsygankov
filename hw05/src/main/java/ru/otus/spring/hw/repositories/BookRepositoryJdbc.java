package ru.otus.spring.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private final GenreRepository genreRepository;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRelationsRepository genreRelationsRepository;

    @Override
    public Optional<Book> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            List<Book> books = jdbcOperations.query(
                    "SELECT " +
                            "books.id AS book_id, " +
                            "title AS book_title, " +
                            "author_id AS author_id, " +
                            "full_name AS author_name, " +
                            "genres.id AS genre_id, " +
                            "genres.name AS genre_name " +
                            "FROM books " +
                            "LEFT JOIN authors ON authors.id = books.author_id " +
                            "LEFT JOIN books_genres ON books.id = books_genres.book_id " +
                            "LEFT JOIN genres ON books_genres.genre_id = genres.id " +
                            "WHERE books.id = :id", params, new BookResultSetExtractor());
            return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return getAllBooksWithoutGenres();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        jdbcOperations.update(
                "DELETE FROM books_genres WHERE book_id = :id", params);

        jdbcOperations.update(
                "DELETE FROM books WHERE id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbcOperations.query(
                "SELECT " +
                        "books.id AS book_id, " +
                        "title AS book_title, " +
                        "author_id AS author_id, " +
                        "full_name AS author_name, " +
                        "genres.id AS genre_id, " +
                        "genres.name AS genre_name " +
                        "FROM books " +
                        "LEFT JOIN authors ON authors.id = books.author_id " +
                        "LEFT JOIN books_genres ON books.id = books_genres.book_id " +
                        "LEFT JOIN genres ON books_genres.genre_id = genres.id",
                new BookResultSetExtractor()
        );
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());

        var keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(
                "INSERT INTO books (title, author_id) VALUES (:title, :authorId)", params, keyHolder);

        Number generatedKey = keyHolder.getKeyAs(Long.class);
        if (generatedKey != null) {
            long id = generatedKey.longValue();
            book.setId(id);
        }
        genreRelationsRepository.insertGenreRelationsForBookId(book.getId(),
                book.getGenres().stream().map(Genre::getId).toList());
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());

        jdbcOperations.update(
                "UPDATE books SET title = :title, author_id = :authorId WHERE id = :id", params);

        genreRelationsRepository.removeGenresRelationsForBookId(book.getId());
        genreRelationsRepository.insertGenreRelationsForBookId(book.getId(),
                book.getGenres().stream().map(Genre::getId).toList());

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("book_id"), rs.getString("book_title"),
                    new Author(rs.getLong("author_id"), rs.getString("author_name")), new ArrayList<>());
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                Book book = findBookById(books, bookId);
                if (book == null) {
                    book = new BookRowMapper().mapRow(rs, 0);
                    books.add(book);
                }
                long genreId = rs.getLong("genre_id");
                if (!rs.wasNull()) {
                    Genre genre = new Genre(genreId, rs.getString("genre_name"));
                    book.getGenres().add(genre);
                }
            }
            return books;
        }

        private Book findBookById(List<Book> books, long bookId) {
            return books.stream()
                    .filter(book -> book.getId() == bookId)
                    .findFirst()
                    .orElse(null);
        }
    }
}