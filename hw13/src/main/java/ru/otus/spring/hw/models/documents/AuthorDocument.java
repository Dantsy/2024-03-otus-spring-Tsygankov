package ru.otus.spring.hw.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "authors")
public class AuthorDocument {

    @Id
    private String id;

    private String fullName;
}
