package ru.otus.spring.hw.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;

    private String title;

    private AuthorDocument author;

    private List<GenreDocument> genres;
}