package ru.otus.spring.hw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.services.AuthorService;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;
import ru.otus.spring.hw.services.GenreService;

import java.util.Optional;

@Controller
public class BookPagesController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final DtoMapper mapper;

    @Autowired
    public BookPagesController(BookService bookService,
                               AuthorService authorService,
                               GenreService genreService,
                               CommentService commentService,
                               DtoMapper mapper) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @GetMapping(path = {"/books/list", "/books"})
    public String bookList() {
        return "book-list-ajax";
    }

    @GetMapping("/books/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDtoIds book = fetchBook(id).orElseGet(BookDtoIds::new);
        model.addAttribute("book", book);
        fillDataInModel(model, book.getId());
        return "book-edit-ajax";
    }

    private Optional<BookDtoIds> fetchBook(long id) {
        if (id == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.bookDtoToBookDtoIds(bookService.findById(id)));
    }

    private void fillDataInModel(Model model, long bookId) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("comments", commentService.findCommentsByBookId(bookId));
    }
}