package ru.otus.spring.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DtoMapper {


    BookDto bookToBookDTO(Book book);

    AuthorDto authorToAuthorDto(Author author);

    GenreDto genreToGenreDto(Genre genre);

    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "commentIds", expression = "java(extractCommentDtoIds(book.getComments()))")
    @Mapping(target = "genreIds", expression = "java(extractGenreDtoIds(book.getGenres()))")
    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookDtoIds bookDtoToBookDtoIds(BookDto book);

    default List<Long> extractGenreDtoIds(List<GenreDto> genres) {
        return genres.stream()
                .map(GenreDto::getId)
                .collect(Collectors.toList());
    }

    default List<Long> extractCommentDtoIds(List<CommentDto> comments) {
        return comments.stream()
                .map(CommentDto::getId)
                .collect(Collectors.toList());
    }
}
