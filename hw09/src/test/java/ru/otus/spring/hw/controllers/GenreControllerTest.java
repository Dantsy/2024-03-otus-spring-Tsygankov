package ru.otus.spring.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import ru.otus.spring.hw.mappers.DtoMapperImpl;
import ru.otus.spring.hw.repositories.TestDataHolder;
import ru.otus.spring.hw.services.GenreService;

import static org.mockito.BDDMockito.given;

@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc
@Import({DtoMapperImpl.class})
public class GenreControllerTest {

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
    public void shouldReturnCorrectGenreList() throws Exception {
        var genreDtoList = TestDataHolder.getGenres().stream().map(mapper::genreToGenreDto).toList();
        given(genreService.findAll()).willReturn(genreDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/genres/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("genre-list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("genres"))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", Matchers.containsInAnyOrder(genreDtoList.toArray())));
    }
}