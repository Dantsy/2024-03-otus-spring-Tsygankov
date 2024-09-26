package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String BOOK_NOT_FOUND_MESSAGE = "Book with id %s not found";

    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment index %d for book with id %s not found";

    private final BookRepository bookRepository;

    @Override
    public List<String> findCommentsByBookId(String bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        return book.getComments();
    }

    @Override
    public Optional<String> findCommentByBookIdAndIndex(String bookId, int index) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        return Optional.of(book.getComments().get(index));
    }

    @Override
    public String add(String bookId, String content) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        List<String> bookComments = book.getComments();
        bookComments.add(content);
        bookRepository.save(book);
        return bookComments.get(bookComments.size() - 1);
    }

    @Override
    public String updateByBookIdAndIndex(String bookId, int index, String content) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        List<String> bookComments = book.getComments();
        if (index > bookComments.size()) {
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(index, bookId));
        }
        book.getComments().set(index, content);
        bookRepository.save(book);
        return bookComments.get(index);
    }

    @Override
    public void deleteByBookIdAndIndex(String bookId, int index) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        List<String> bookComments = book.getComments();
        if (index > bookComments.size()) {
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(index, bookId));
        }
        bookComments.remove(index);
        bookRepository.save(book);
    }

}
