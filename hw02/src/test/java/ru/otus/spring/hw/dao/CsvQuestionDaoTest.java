package ru.otus.spring.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.domain.Question;

import java.util.List;

public class CsvQuestionDaoTest {
    private static final int qListSize = 1;
    private static final int questionSize = 3;
    private static final String question = "Which planet is known as the Red Planet?";
    private static final String csvPath = "questions.csv";
    private TestFileNameProvider testFileNameProvider;

    @BeforeEach
    public void init() {
        testFileNameProvider = Mockito.mock(TestFileNameProvider.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn(csvPath);
    }

    @Test
    public void checkThatQuestionReadFromFileIsCorrect() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
        List<Question> questionList = csvQuestionDao.findAll();
        Assertions.assertEquals(qListSize, questionList.size());
        Assertions.assertTrue(questionList.get(0).text().equals(question));
        Assertions.assertEquals(questionList.get(0).answers().size(), questionSize);
    }
}