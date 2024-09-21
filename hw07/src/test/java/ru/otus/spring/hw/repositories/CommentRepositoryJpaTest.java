package ru.otus.spring.hw.repositories;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.models.Book;
import ru.otus.spring.hw.models.Comment;

import java.util.List;
import java.util.Objects;

@DisplayName("Jpa based repository for working with comments")
@DataJpaTest
@Import({DtoMapperImpl.class})
@ActiveProfiles("test")
class CommentRepositoryJpaTest {

    private static final int FIRST_COMMENT_INDEX = 0;
    private static final int FIRST_BOOK_INDEX = 0;
    private static final long FIRST_COMMENT_ID = 1L;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should receive a comment by id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedCommentDto = mapper.commentToCommentDto(
                TestDataHolder.getBooks().get(FIRST_BOOK_INDEX).getComments().get(FIRST_COMMENT_INDEX));
        var actualCommentDto = commentRepository.findById(expectedCommentDto.getId()).map(mapper::commentToCommentDto);
        Assertions.assertThat(actualCommentDto).isPresent()
                .get().isEqualTo(expectedCommentDto);
    }

    @DisplayName("must save a new comment for the book")
    @Test
    void shouldSaveNewComment() {
        var book = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var expectedComment = new Comment(0, "Comment_%d_for_book_%d".formatted(1, book.getId()), book);
        var expectedCommentDto = mapper.commentToCommentDto(expectedComment);
        var returnedCommentDto = mapper.commentToCommentDto(commentRepository.save(expectedComment));
        Assertions.assertThat(returnedCommentDto).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringExpectedNullFields()
                .isEqualTo(expectedCommentDto);

        Assertions.assertThat(mapper.commentToCommentDto(entityManager.find(Comment.class, returnedCommentDto.getId())))
                .isEqualTo(returnedCommentDto);
    }

    @DisplayName("must save the modified comment")
    @Test
    void shouldSaveUpdatedComment() {
        var book = TestDataHolder.getBooks().get(FIRST_BOOK_INDEX);
        var expectedComment = new Comment(1L, "Content of comment that not expected", book);
        var expectedCommentDto = mapper.commentToCommentDto(expectedComment);

        Assertions.assertThat(mapper.commentToCommentDto(entityManager.find(Comment.class, expectedCommentDto.getId())))
                .isNotEqualTo(expectedCommentDto);

        var returnedComment = commentRepository.save(expectedComment);
        var returnedCommentDto = mapper.commentToCommentDto(returnedComment);

        Assertions.assertThat(returnedCommentDto).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(expectedCommentDto);

        Assertions.assertThat(mapper.commentToCommentDto(entityManager.find(Comment.class, returnedCommentDto.getId())))
                .isEqualTo(returnedCommentDto);
    }

    @DisplayName("should delete a comment by id")
    @Test
    void shouldDeleteComment() {
        Assertions.assertThat(entityManager.find(Comment.class, FIRST_COMMENT_ID)).isNotNull();
        commentRepository.deleteById(FIRST_COMMENT_ID);
        Assertions.assertThat(entityManager.find(Comment.class, FIRST_COMMENT_ID)).isNull();
    }

    @DisplayName("should delete all comments after deleting a book")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldDeleteAllCommentsForBook(Book expectedBook) {
        var actualCommentsDto = expectedBook.getComments().stream().map(
                comment -> entityManager.find(Comment.class, comment.getId())).map(mapper::commentToCommentDto).toList();
        var expectedComments = expectedBook.getComments().stream().map(mapper::commentToCommentDto).toList();

        Assertions.assertThat(actualCommentsDto).usingRecursiveComparison().isEqualTo(expectedComments);
        actualCommentsDto.forEach(System.out::println);

        entityManager.remove(entityManager.find(Book.class, expectedBook.getId()));

        var actualCommentsAfterDelete = expectedComments.stream().map(commentDto -> entityManager.find(Comment.class, commentDto.getId()))
                .filter(Objects::nonNull).toList();

        Assertions.assertThat(actualCommentsAfterDelete).isEmpty();
    }

    @DisplayName("should return empty Optional for non-existing comment")
    @Test
    void shouldReturnEmptyOptionalForNonExistingComment() {
        var nonExistingComment = commentRepository.findById(9999L);
        Assertions.assertThat(nonExistingComment).isNotPresent();
    }

    @DisplayName("should find all comments by book id")
    @Test
    void shouldFindAllCommentsByBookId() {
        long bookId = 1L;

        List<Comment> foundComments = commentRepository.findAllByBookId(bookId);

        Assertions.assertThat(foundComments).isNotEmpty();
        Assertions.assertThat(foundComments).extracting(Comment::getBook)
                .extracting(Book::getId)
                .containsOnly(bookId);
    }

    public static List<Book> getBooks() {
        return TestDataHolder.getBooks();
    }
}