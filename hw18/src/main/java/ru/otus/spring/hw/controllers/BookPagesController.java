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
    public Mono<String> showBookList() {
        return Mono.just("book-list-ajax");
    }

    @GetMapping("/books/edit")
    public Mono<String> showEditPage(@RequestParam("id") long id, Model model) {
        Mono<BookDtoIds> bookDtoIdsMono = (id == 0)
                ? Mono.just(new BookDtoIds())
                : bookService.findByIdWithDetails(id)
                .map(mapper::bookDtoToBookDtoIds)
                .switchIfEmpty(Mono.just(new BookDtoIds()));

        return bookDtoIdsMono
                .flatMap(bookDtoIds -> {
                    model.addAttribute("book", bookDtoIds);
                    return Mono.zip(
                            authorService.findAll().collectList(),
                            genreService.findAll().collectList(),
                            commentService.findCommentsByBookId(bookDtoIds.getId()).collectList()
                    ).doOnNext(tuple -> {
                        model.addAttribute("authors", tuple.getT1());
                        model.addAttribute("genres", tuple.getT2());
                        model.addAttribute("comments", tuple.getT3());
                    }).thenReturn("book-edit-ajax");
                });
    }
}