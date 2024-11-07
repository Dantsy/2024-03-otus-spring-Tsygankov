package ru.otus.spring.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.repositories.TestDataHolder;
import ru.otus.spring.hw.services.AuthorService;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;
import ru.otus.spring.hw.services.GenreService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthorPagesController.class)
@AutoConfigureMockMvc
@Import({DtoMapperImpl.class})
public class AuthorControllerTest {

    private static final String AUTHOR_API_PATH = "/authors";
    private static final int FIRST_AUTHOR_INDEX = 0;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private DtoMapperImpl mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @Test
    @DisplayName("Should return correct author list")
    public void ShouldReturnCorrectAuthorList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_PATH + "/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("author-list-ajax"));
    }

    @Test
    @DisplayName("Should return correct edit page for new author")
    public void ShouldReturnCorrectAuthorEditPageForNewAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_PATH + "/edit")
                        .param("author_href", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("author-edit-ajax"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("author", "author_href"))
                .andExpect(MockMvcResultMatchers.model().attribute("author", new AuthorDto()))
                .andExpect(MockMvcResultMatchers.model().attribute("author_href", ""));
    }

    @Test
    @DisplayName("Should return error when rest template throws exception")
    public void ShouldReturnErrorWhenRestTemplateThrowsException() throws Exception {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_PATH + "/edit")
                        .param("author_href", "http://localhost:8080/api/authors/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}