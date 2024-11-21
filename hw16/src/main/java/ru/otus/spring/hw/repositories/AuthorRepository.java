package ru.otus.spring.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.hw.models.Author;

@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends CrudRepository<Author, Long> {

}
