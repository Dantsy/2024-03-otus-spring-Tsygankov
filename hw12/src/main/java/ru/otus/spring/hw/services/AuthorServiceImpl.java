package ru.otus.spring.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final DtoMapper mapper;

    @PreAuthorize("isAuthenticated()")
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(mapper::authorToAuthorDto).toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public AuthorDto findById(long id) {
        return authorRepository.findById(id)
                .map(mapper::authorToAuthorDto)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AuthorDto insert(String fullName) {
        return mapper.authorToAuthorDto(save(0, fullName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AuthorDto update(long id, String fullName) {
        return mapper.authorToAuthorDto(save(id, fullName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteById(long id) {
        List<Book> books = bookRepository.findByAuthorId(id);

        bookRepository.deleteAll(books);

        authorRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Author save(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }
}