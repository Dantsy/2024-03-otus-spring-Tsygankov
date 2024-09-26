package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw.models.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
