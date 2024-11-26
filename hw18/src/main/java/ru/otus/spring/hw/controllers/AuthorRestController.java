package ru.otus.spring.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.dtos.AuthorDto;
import ru.otus.spring.hw.services.AuthorService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping(path = "/api/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/api/authors/{id}")
    public Mono<ResponseEntity<AuthorDto>> getAuthorById(@PathVariable("id") long id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/authors")
    public Mono<ResponseEntity<Object>> saveAuthor(@Validated @RequestBody Mono<AuthorDto> authorDtoMono) {
        return authorDtoMono
                .flatMap(authorDto -> authorService.update(authorDto.getId(), authorDto.getFullName())
                        .map(updatedAuthorDto -> ResponseEntity.ok((Object) updatedAuthorDto))
                        .onErrorResume(WebExchangeBindException.class, ex -> {
                            Map<String, String> errors = new HashMap<>();
                            for (FieldError error : ex.getFieldErrors()) {
                                errors.put(error.getField(), error.getDefaultMessage());
                            }
                            return Mono.just(ResponseEntity.badRequest().body(errors));
                        }));
    }

    @DeleteMapping("/api/authors/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable("id") long id) {
        return authorService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    private Mono<ResponseEntity<Object>> handleValidationErrors(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }
}