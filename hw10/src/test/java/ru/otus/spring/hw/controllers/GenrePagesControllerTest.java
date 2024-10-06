package ru.otus.spring.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GenrePagesController.class)
@AutoConfigureMockMvc
public class GenrePagesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should return correct genre list")
    public void ShouldReturnCorrectGenreList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/genres/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("genre-list-ajax"));
    }
}