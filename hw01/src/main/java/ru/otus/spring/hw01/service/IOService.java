package ru.otus.spring.hw01.service;

import ru.otus.spring.hw01.domain.Question;

import java.util.List;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    void printQuestion(Question question);

    void printQuestions(List<Question> questions);
}
