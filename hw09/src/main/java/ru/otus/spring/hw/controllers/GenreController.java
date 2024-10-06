package ru.otus.spring.hw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.services.GenreService;

import java.util.List;

@Controller
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(path = {"/genres/list", "/genres"})
    public String genreList(Model model) {
        List<GenreDto> genreDtos = genreService.findAll();
        model.addAttribute("genres", genreDtos);
        return "genre-list";
    }
}