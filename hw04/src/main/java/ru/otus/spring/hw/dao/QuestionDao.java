package ru.otus.spring.hw.dao;

import ru.otus.spring.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
