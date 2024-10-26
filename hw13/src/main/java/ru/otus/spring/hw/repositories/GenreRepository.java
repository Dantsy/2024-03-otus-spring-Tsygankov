package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw.models.documents.GenreDocument;

public interface GenreRepository extends MongoRepository<GenreDocument, String> {
}