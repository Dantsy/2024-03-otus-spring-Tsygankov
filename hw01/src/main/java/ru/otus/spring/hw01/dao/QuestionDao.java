package ru.otus.spring.hw01.dao;

import ru.otus.spring.hw01.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
