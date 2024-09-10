package ru.otus.spring.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(target = "comments", ignore = true)
    BookDto bookToBookDTO(final Book book);

    AuthorDto authorToAuthorDto(final Author author);

    GenreDto genreToGenreDto(final Genre genre);

    CommentDto commentToCommentDto(final Comment comment);

}
