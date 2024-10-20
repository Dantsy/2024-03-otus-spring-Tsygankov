package ru.otus.spring.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.repositories.TestDataHolder;
import ru.otus.spring.hw.services.AuthorService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Controller for working with authors")
@WebMvcTest(AuthorController.class)
@Import({DtoMapperImpl.class})
public class AuthorControllerTest {

    private static final int FIRST_AUTHOR_INDEX = 0;

    public static final String AUTHORS_URL = "/authors";

    public static final String AUTHOR_EDIT_URL = "/authors/edit";

    public static final String AUTHOR_LIST_URL = "/authors/list";

    public static final String AUTHORS_DELETE_URL = "/authors/delete";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

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
    @WithMockUser(authorities = {"admin"})
    public void ShouldReturnCorrectAuthorList() throws Exception {
        var authorDtoList = TestDataHolder.getAuthors().stream().map(author -> mapper.authorToAuthorDto(author)).toList();
        given(authorService.findAll()).willReturn(authorDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_LIST_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("author-list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("authors"))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", Matchers.containsInAnyOrder(authorDtoList.toArray())));
    }

    @Test
    @DisplayName("Should return correct edit page with attributes")
    @WithMockUser(authorities = {"admin"})
    public void ShouldReturnCorrectBookEditPageWithAttributes() throws Exception {
        var expectedAuthor = TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX);
        var expectedAuthorDto = mapper.authorToAuthorDto(expectedAuthor);
        given(authorService.findById(1)).willReturn(expectedAuthorDto);
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_EDIT_URL).param("id", Long.toString(expectedAuthorDto.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("author-edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("author"))
                .andExpect(MockMvcResultMatchers.model().attribute("author", Matchers.is(expectedAuthorDto)));
    }

    @Test
    @DisplayName("Should return error when send invalid id")
    @WithMockUser(authorities = {"admin"})
    public void ShouldReturnErrorInvalidId() throws Exception {
        given(authorService.findById(2)).willThrow(new EntityNotFoundException("Author with id %d not found".formatted(2)));
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_EDIT_URL).param("id", "2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("Should return error when send invalid author object")
    @WithMockUser(authorities = {"admin"})
    public void ShouldReturnErrorInvalidAuthorContent() throws Exception {
        var expectedAuthorDto = new AuthorDto();
        mockMvc.perform(MockMvcRequestBuilders.post(AUTHOR_EDIT_URL)
                        .param("author", objectMapper.writeValueAsString(expectedAuthorDto))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.model().attributeHasErrors("author"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("author", "fullName"));
    }

    @Test
    @DisplayName("Should call delete method in repository")
    @WithMockUser(authorities = {"admin"})
    public void ShouldCallDeleteMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHORS_DELETE_URL)
                .param("id", "0"));
        verify(authorService, times(1)).deleteById(0L);
    }

    @DisplayName("Security - authenticated. Should return successful status")
    @Test
    @WithMockUser(authorities = {"admin"})
    public void testAuthenticatedOnAdmin() throws Exception {
        var expectedAuthor = TestDataHolder.getAuthors().get(FIRST_AUTHOR_INDEX);
        var expectedAuthorDto = mapper.authorToAuthorDto(expectedAuthor);
        given(authorService.findById(1)).willReturn(expectedAuthorDto);
        mockMvc.perform(get(AUTHORS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(AUTHOR_EDIT_URL).param("id", Long.toString(expectedAuthorDto.getId())))
                .andExpect(status().isOk());
    }

    @DisplayName("Security - unauthenticated. Should return 401 (unauthorized) status")
    @Test
    @WithAnonymousUser
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get(AUTHORS_URL))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(AUTHOR_EDIT_URL).param("id", "0"))
                .andExpect(status().isUnauthorized());
    }

}