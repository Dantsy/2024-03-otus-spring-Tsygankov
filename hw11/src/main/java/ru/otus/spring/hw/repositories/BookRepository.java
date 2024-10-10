package ru.otus.spring.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.spring.hw.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

}
