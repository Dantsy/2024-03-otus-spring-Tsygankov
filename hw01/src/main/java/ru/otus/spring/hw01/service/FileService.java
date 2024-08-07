package ru.otus.spring.hw01.service;

import java.io.Reader;

public interface FileService {
    Reader getReader(String filename);
}
