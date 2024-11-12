package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final DtoMapper mapper;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(mapper::genreToGenreDto).toList();
    }
}