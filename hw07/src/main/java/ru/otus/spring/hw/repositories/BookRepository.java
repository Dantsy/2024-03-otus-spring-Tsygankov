package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.hw.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
