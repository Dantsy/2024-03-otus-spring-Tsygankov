package ru.otus.spring.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.dtos.BookDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(BookDto bookDto) {
        var genresString = bookDto.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.authorToString(bookDto.getAuthor()),
                genresString);
    }

    public String bookToStringWithComments(BookDto bookDto) {
        var commentsToString = bookDto.getComments().stream()
                .map(commentConverter::commentToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, comments: [%s]".formatted(
                bookDto.getId(),
                bookDto.getTitle(),
                authorConverter.authorToString(bookDto.getAuthor()),
                commentsToString);
    }
}
