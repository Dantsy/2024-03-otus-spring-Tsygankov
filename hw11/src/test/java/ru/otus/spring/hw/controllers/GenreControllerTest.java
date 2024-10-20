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
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.repositories.TestDataHolder;
import ru.otus.spring.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Controller for working with genres")
@WebMvcTest(GenreController.class)
@Import({DtoMapperImpl.class})
public class GenreControllerTest {

    public static final String GENRES_URL = "/genres";
    public static final String GENRES_LIST_URL = "/genres/list";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private DtoMapperImpl mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        TestDataHolder.prepareTestData();
    }

    @Test
    @DisplayName("Should return correct genre list")
    @WithMockUser(authorities = {"user"})
    public void ShouldReturnCorrectGenreList() throws Exception {
        List<GenreDto> genreDtoList = TestDataHolder.getGenres().stream().map(mapper::genreToGenreDto).toList();
        given(genreService.findAll()).willReturn(genreDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get(GENRES_LIST_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("genre-list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("genres"))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", Matchers.containsInAnyOrder(genreDtoList.toArray())));
    }

    @DisplayName("Security - authenticated. Should return successful status")
    @Test
    @WithMockUser(authorities = {"user"})
    public void testAuthenticatedOnUser() throws Exception {
        List<GenreDto> genreDtoList = TestDataHolder.getGenres().stream().map(mapper::genreToGenreDto).toList();
        given(genreService.findAll()).willReturn(genreDtoList);
        mockMvc.perform(get(GENRES_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(GENRES_LIST_URL))
                .andExpect(status().isOk());
    }

    @DisplayName("Security - unauthenticated. Should return 401 (unauthorized) status")
    @Test
    @WithAnonymousUser
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get(GENRES_URL))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(GENRES_LIST_URL))
                .andExpect(status().isUnauthorized());
    }
}