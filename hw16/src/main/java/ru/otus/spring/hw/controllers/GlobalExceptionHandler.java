package ru.otus.spring.hw.controllers;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.spring.hw.exceptions.EntityNotFoundException;

import java.util.Map;

@ControllerAdvice
@Data
public class GlobalExceptionHandler {

    private long exceptionNotFoundCounter;

    private long totalExceptionsCounter;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        totalExceptionsCounter++;
        exceptionNotFoundCounter++;
        return ResponseEntity.badRequest().body(Map.of("id", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAnyOtherException(Exception ex) {
        totalExceptionsCounter++;
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}