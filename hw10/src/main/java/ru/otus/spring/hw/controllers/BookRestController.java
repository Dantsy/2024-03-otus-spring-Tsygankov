package ru.otus.spring.hw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.hw.dtos.BookDto;
import ru.otus.spring.hw.dtos.BookDtoIds;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookRestController {

    private final BookService bookService;
    private final CommentService commentService;

    @Autowired
    public BookRestController(BookService bookService, CommentService commentService) {
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getAllBookList() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        BookDto bookDto = bookService.findById(id);
        if (bookDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> saveBook(@Validated @RequestBody BookDtoIds book,
                                      BindingResult bindingResult,
                                      @RequestParam(name = "newCommentContent", required = false) String newCommentContent) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        BookDto savedBook = bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreIds(), book.getCommentIds());

        if (newCommentContent != null && !newCommentContent.isEmpty()) {
            commentService.insert(savedBook.getId(), newCommentContent);
        }

        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
        BookDto bookDto = bookService.findById(id);
        if (bookDto == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}