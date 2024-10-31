package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw.models.documents.BookDocument;

public interface BookRepository extends MongoRepository<BookDocument, String> {
}