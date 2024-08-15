package ru.otus.spring.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppConfig implements TestFileNameProvider {
    private String testFileName;
}
