package ru.otus.spring.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.hw.dtos.GenreDto;
import ru.otus.spring.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class GenreController {

    private final GenreService genreService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = {"/genres/list", "/genres"})
    public String genreList(Model model) {
        List<GenreDto> genreDtos = genreService.findAll();
        model.addAttribute("genres", genreDtos);
        return "genre-list";
    }
}