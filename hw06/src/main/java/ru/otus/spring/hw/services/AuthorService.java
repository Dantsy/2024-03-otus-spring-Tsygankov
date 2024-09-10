package ru.otus.spring.hw.services;

import ru.otus.spring.hw.dtos.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
