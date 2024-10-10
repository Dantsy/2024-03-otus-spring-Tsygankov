package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final DtoMapper mapper;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(mapper::authorToAuthorDto).toList();
    }

    @Override
    public AuthorDto findById(long id) {
        return authorRepository.findById(id).map(mapper::authorToAuthorDto)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    @Override
    public AuthorDto insert(String fullName) {
        return mapper.authorToAuthorDto(save(0, fullName));
    }

    @Override
    public AuthorDto update(long id, String fullName) {
        return mapper.authorToAuthorDto(save(id, fullName));
    }

    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Author save(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }
}