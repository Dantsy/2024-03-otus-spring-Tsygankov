package ru.otus.spring.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.models.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.Optional;
import java.util.stream.Collectors;

@DisplayName("Service for working with comments")
@SpringBootTest(classes = {CommentServiceImpl.class, CommentRepository.class, BookRepository.class, DtoMapperImpl.class})
@ActiveProfiles("test")
class CommentServiceTest {

    private static final int FIRST_COMMENT_INDEX = 0;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @DisplayName("should return a list of comments by book id")
    @Test
    void findCommentsByBookId() {
        var expectedBook = TestDataHolder.getBooks().get(0);
        var expectedComments = expectedBook.getComments();
        Mockito.when(commentRepository.findAllByBookId(Mockito.anyLong())).thenReturn(expectedComments);

        var actualCommentDtos = commentService.findCommentsByBookId(expectedBook.getId());

        Assertions.assertThat(actualCommentDtos).isNotNull()
                .hasSize(expectedComments.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedComments.stream().map(mapper::commentToCommentDto).collect(Collectors.toList()));

        Mockito.verify(commentRepository, Mockito.times(1)).findAllByBookId(expectedBook.getId());
    }

    @DisplayName("should return comment by id")
    @Test
    void findById() {
        var expectedComment = TestDataHolder.getBooks().get(0).getComments().get(FIRST_COMMENT_INDEX);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedComment));

        var actualCommentDto = commentService.findById(expectedComment.getId());

        Assertions.assertThat(actualCommentDto).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mapper.commentToCommentDto(expectedComment));

        Mockito.verify(commentRepository, Mockito.times(1)).findById(expectedComment.getId());
    }

    @DisplayName("must create a new comment")
    @Test
    void insert() {
        var expectedBook = TestDataHolder.getBooks().get(0);
        var expectedComment = new Comment(0, "New Comment", expectedBook);
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedBook));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenAnswer(invocation -> {
            Comment sourceComment = invocation.getArgument(0);
            return new Comment(1, sourceComment.getContent(), sourceComment.getBook());
        });

        var actualCommentDto = commentService.insert(expectedBook.getId(), expectedComment.getContent());

        Assertions.assertThat(actualCommentDto).isNotNull()
                .matches(commentDto -> commentDto.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(mapper.commentToCommentDto(expectedComment));

        Mockito.verify(bookRepository, Mockito.times(1)).findById(expectedBook.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @DisplayName("should update the comment")
    @Test
    void updateById() {
        var expectedComment = TestDataHolder.getBooks().get(0).getComments().get(FIRST_COMMENT_INDEX);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedComment));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var actualCommentDto = commentService.updateById(expectedComment.getId(), "Updated Comment");

        Assertions.assertThat(actualCommentDto).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mapper.commentToCommentDto(expectedComment));

        Mockito.verify(commentRepository, Mockito.times(1)).findById(expectedComment.getId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @DisplayName("should delete a comment by id")
    @Test
    void deleteById() {
        var expectedComment = TestDataHolder.getBooks().get(0).getComments().get(FIRST_COMMENT_INDEX);

        commentService.deleteById(expectedComment.getId());

        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(expectedComment.getId());
    }
}