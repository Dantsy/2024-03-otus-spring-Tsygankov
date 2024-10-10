package ru.otus.spring.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.services.AuthorService;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;
import ru.otus.spring.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class BookPagesController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final DtoMapper mapper;

    @GetMapping(path = {"/books/list", "/books"})
    public Mono<String> bookList() {
        return Mono.just("book-list-ajax");
    }

    @GetMapping("/books/edit")
    public Mono<String> editPage(@RequestParam("id") long id, Model model) {
        return bookService.findByIdWithDetails(id)
                .map(mapper::bookDtoToBookDtoIds)
                .switchIfEmpty(Mono.just(new BookDtoIds()))
                .doOnNext(bookDtoIds -> {
                    model.addAttribute("book", bookDtoIds);
                    model.addAttribute("authors", authorService.findAll());
                    model.addAttribute("genres", genreService.findAll());
                    model.addAttribute("comments", commentService.findCommentsByBookId(bookDtoIds.getId()));
                })
                .thenReturn("book-edit-ajax");
    }

}
