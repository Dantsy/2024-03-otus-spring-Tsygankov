package ru.otus.spring.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.services.AuthorService;

@RequiredArgsConstructor
@Controller
public class AuthorPagesController {

    private final AuthorService authorService;

    @GetMapping(path = {"/authors/list", "/authors"})
    public Mono<String> authorList() {
        return Mono.just("author-list-ajax");
    }

    @GetMapping("/authors/edit")
    public Mono<String> editPage(@RequestParam("id") long id, Model model) {
        return authorService.findById(id)
                .switchIfEmpty(Mono.just(new AuthorDto()))
                .doOnNext(updatedAuthorDto -> model.addAttribute("author", updatedAuthorDto))
                .thenReturn("author-edit-ajax");
    }

}
