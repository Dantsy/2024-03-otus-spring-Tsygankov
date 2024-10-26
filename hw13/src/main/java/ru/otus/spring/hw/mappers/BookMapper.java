package ru.otus.spring.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.models.documents.AuthorDocument;
import ru.otus.spring.hw.models.documents.BookDocument;
import ru.otus.spring.hw.models.documents.GenreDocument;
import ru.otus.spring.hw.models.entities.Book;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;

    public BookMapper(AuthorMapper authorMapper, GenreMapper genreMapper) {
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
    }

    public BookDocument toDocument(Book book) {
        AuthorDocument authorDocument = authorMapper.toDocument(book.getAuthor());
        List<GenreDocument> genreDocuments = book.getGenres().stream()
                .map(genreMapper::toDocument)
                .collect(Collectors.toList());

        return new BookDocument(
                book.getId().toString(),
                book.getTitle(),
                authorDocument,
                genreDocuments
        );
    }
}