package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final DtoMapper mapper;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll().map(mapper::authorToAuthorDto);
    }

    @Override
    public Mono<AuthorDto> findById(long id) {
        return authorRepository.findById(id).map(mapper::authorToAuthorDto);
    }

    @Override
    public Mono<AuthorDto> insert(String fullName) {
        return save(0, fullName).map(mapper::authorToAuthorDto);
    }

    @Override
    public Mono<AuthorDto> update(long id, String fullName) {
        return save(id, fullName).map(mapper::authorToAuthorDto);
    }

    @Override
    public Mono<Void> deleteById(long id) {
        return authorRepository.deleteById(id);
    }

    @Override
    public Mono<Author> save(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }
}