package ru.otus.spring.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final DtoMapper mapper;

    @Transactional
    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyBook")
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(mapper::bookToBookDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyBookList")
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(mapper::bookToBookDTO)
                .toList();
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyBook")
    public BookDto insert(String title, long authorId, List<Long> genresIds, List<Long> commentIds) {
        return mapper.bookToBookDTO(save(0, title, authorId, genresIds, commentIds));
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyBook")
    public BookDto update(long id, String title, long authorId, List<Long> genresIds, List<Long> commentIds) {
        return mapper.bookToBookDTO(save(id, title, authorId, genresIds, commentIds));
    }

    @Override
    @CircuitBreaker(name = "repositoryCircuitBreaker", fallbackMethod = "fallbackEmptyBookList")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, List<Long> genresIds, List<Long> commentIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }

        List<Comment> comments;
        if (!Objects.isNull(commentIds) && !commentIds.isEmpty()) {
            comments = commentRepository.findAllByIdIn(commentIds);
            if (isEmpty(comments)) {
                throw new EntityNotFoundException("Comments with ids %s not found".formatted(genresIds));
            }
        } else {
            comments = new ArrayList<>();
        }

        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }

    public BookDto fallbackEmptyBook(Throwable e) {
        return new BookDto();
    }

    public List<BookDto> fallbackEmptyBookList(Throwable e) {
        return new ArrayList<>();
    }
}