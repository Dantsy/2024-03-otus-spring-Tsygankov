package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.dtos.AuthorDto;
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

}
