package ru.otus.spring.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = {"/authors/list", "/authors"})
    public String authorList(Model model) {
        List<AuthorDto> authorDtos = authorService.findAll();
        model.addAttribute("authors", authorDtos);
        return "author-list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/authors/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        AuthorDto author;
        if (!(id == 0)) {
            author = authorService.findById(id);
        } else {
            author = new AuthorDto();
        }
        model.addAttribute("author", author);
        return "author-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authors/edit")
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorDto author,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "author-edit";
        }

        authorService.update(author.getId(), author.getFullName());
        return "redirect:/authors";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}