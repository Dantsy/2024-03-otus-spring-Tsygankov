package ru.otus.spring.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenrePagesController {

    @GetMapping(path = {"/genres/list", "/genres"})
    public String showGenreList() {
        return "genre-list-ajax";
    }

}
