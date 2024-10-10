package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final DtoMapper mapper;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(mapper::genreToGenreDto)
                .onErrorResume(e -> Flux.error(new RuntimeException("Error finding genres: " + e.getMessage())));
    }
}
