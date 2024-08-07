package ru.otus.spring.hw01.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppConfig implements TestFileNameProvider {

    private String testFileName;

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}