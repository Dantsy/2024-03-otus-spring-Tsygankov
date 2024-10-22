package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapper;
import ru.otus.spring.hw.models.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.TestDataHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Service for working with authors")
@ActiveProfiles("test")
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private DtoMapper mapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        author = TestDataHolder.getAuthors().get(0);
        authorDto = new AuthorDto(author.getId(), author.getFullName());
    }

    @Test
    @DisplayName("should return all authors")
    @WithMockUser
    void findAll() throws Exception {
        when(authorRepository.findAll()).thenReturn(TestDataHolder.getAuthors());
        when(mapper.authorToAuthorDto(any(Author.class))).thenReturn(authorDto);

        List<AuthorDto> result = authorService.findAll();

        assertEquals(3, result.size());
        assertEquals(authorDto, result.get(0));

        mockMvc.perform(get("/authors/list"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return author by id")
    @WithMockUser(roles = "ADMIN")
    void findById() throws Exception {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(mapper.authorToAuthorDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.findById(1L);

        assertEquals(authorDto, result);

        mockMvc.perform(get("/authors/edit?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException when author not found by id")
    @WithMockUser
    void findByIdNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.findById(1L));
    }

    @Test
    @DisplayName("should insert new author")
    @WithMockUser(roles = "ADMIN")
    void insert() throws Exception {
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(mapper.authorToAuthorDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.insert("John Doe");

        assertEquals(authorDto, result);

        mockMvc.perform(get("/authors/edit?id=0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should update existing author")
    @WithMockUser(roles = "ADMIN")
    void update() throws Exception {
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(mapper.authorToAuthorDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.update(1L, "John Doe");

        assertEquals(authorDto, result);

        mockMvc.perform(post("/authors/edit")
                        .param("id", "1")
                        .param("fullName", "John Doe"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("should delete author by id")
    @WithMockUser(roles = "ADMIN")
    void deleteById() throws Exception {
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteById(1L);

        verify(authorRepository, times(1)).deleteById(1L);

        mockMvc.perform(get("/authors/delete?id=1"))
                .andExpect(status().is3xxRedirection());
    }
}