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
    public Mono<String> showAuthorList() {
        return Mono.just("author-list-ajax");
    }

    @GetMapping("/authors/edit")
    public Mono<String> showEditPage(@RequestParam("id") long id, Model model) {
        Mono<AuthorDto> authorMono = (id == 0)
                ? Mono.just(new AuthorDto())
                : authorService.findById(id).switchIfEmpty(Mono.just(new AuthorDto()));

        return authorMono
                .doOnNext(author -> model.addAttribute("author", author))
                .thenReturn("author-edit-ajax");
    }
}
