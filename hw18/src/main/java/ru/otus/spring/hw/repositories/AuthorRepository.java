package ru.otus.spring.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.spring.hw.models.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

}