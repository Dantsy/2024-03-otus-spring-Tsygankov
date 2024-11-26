package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.BookRepositoryEnhanced;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookRepositoryEnhanced bookRepositoryEnhanced;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final DtoMapper mapper;

    @Override
    public Mono<BookDto> findByIdWithDetails(Long id) {
        return bookRepositoryEnhanced.findByIdWithDetails(id)
                .map(mapper::bookToBookDTO)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    public Flux<BookDto> findAllWithDetails() {
        return bookRepositoryEnhanced.findAllWithDetails()
                .map(mapper::bookToBookDTO);
    }

    @Override
    public Mono<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .map(mapper::bookToBookDTO)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(mapper::bookToBookDTO);
    }

    @Override
    public Mono<Void> deleteById(long id) {
        return bookRepository.deleteById(id);
    }

    @Override
    public Mono<BookDto> save(long id, String title, long authorId, List<Long> genresIds, List<Long> commentIds) {
        Mono<Author> authorMono = authorId != 0 ? authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId))))
                : Mono.empty();

        Flux<Genre> genresFlux = !genresIds.isEmpty() ? genreRepository.findAllByIdIn(genresIds)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds))))
                : Flux.empty();

        Flux<Comment> commentsFlux = !commentIds.isEmpty() ? commentRepository.findAllByIdIn(commentIds)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException("Comments with ids %s not found".formatted(commentIds))))
                : Flux.empty();

        return Mono.zip(authorMono, genresFlux.collectList(), commentsFlux.collectList())
                .flatMap(objects -> {
                    Author author = objects.getT1();
                    List<Genre> genres = objects.getT2();
                    List<Comment> comments = objects.getT3();
                    var book = new Book(id, title, author, genres, comments);
                    return bookRepositoryEnhanced.save(book);
                })
                .map(mapper::bookToBookDTO);
    }
}