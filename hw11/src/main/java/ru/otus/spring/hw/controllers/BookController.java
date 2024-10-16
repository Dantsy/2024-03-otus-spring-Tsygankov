package ru.otus.spring.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.h2.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.dtos.CommentDto;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.services.AuthorService;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;
import ru.otus.spring.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final DtoMapper mapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = {"/books/list", "/books"})
    public String bookList(Model model) {
        List<BookDto> booksDto = bookService.findAll();
        model.addAttribute("books", booksDto);
        return "book-list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/books/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDtoIds book;
        if (!(id == 0)) {
            book = mapper.bookDtoToBookDtoIds(bookService.findById(id));
        } else {
            book = new BookDtoIds();
        }
        model.addAttribute("book", book);
        fillDataInModel(model, id);
        return "book-edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/edit")
    public ResponseEntity<String> saveBook(@Valid @ModelAttribute("book") BookDtoIds book,
                                           BindingResult bindingResult,
                                           @RequestParam("newCommentContent") String newCommentContent,
                                           Model model) {
        if (bindingResult.hasErrors()) {
            fillDataInModel(model, book.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors");
        }

        var savedBookId = bookService.update(book.getId(), book.getTitle(), book.getAuthorId(),
                book.getGenreIds(), book.getCommentIds()).getId();

        if (!StringUtils.isNullOrEmpty(newCommentContent)) {
            commentService.insert(savedBookId, newCommentContent);
        }

        return ResponseEntity.ok("Book saved successfully");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/books/delete")
    public ResponseEntity<String> deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    private void fillDataInModel(Model model, long bookId) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        List<CommentDto> comments = commentService.findCommentsByBookId(bookId);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("comments", comments);
    }
}