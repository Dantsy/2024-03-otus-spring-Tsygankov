package ru.otus.spring.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw.dtos.CommentDto;

@Component
public class CommentConverter {
    public String commentToString(CommentDto commentDto) {
        return "Id: %d, Content: %s".formatted(commentDto.getId(), commentDto.getContent());
    }
}