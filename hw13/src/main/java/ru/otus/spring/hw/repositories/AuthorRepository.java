package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.hw.models.documents.AuthorDocument;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
}