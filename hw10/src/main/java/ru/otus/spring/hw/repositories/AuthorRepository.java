package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.hw.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}