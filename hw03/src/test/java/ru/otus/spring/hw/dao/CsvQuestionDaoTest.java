package ru.otus.spring.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.domain.Question;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {
    private static final int QUESTION_LIST_SIZE = 1;
    private static final int ANSWER_LIST_SIZE = 3;
    private static final String QUESTION_TEXT = "Which planet is known as the Red Planet?";
    private static final String CSV_PATH = "questions.csv";

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    public void checkThatQuestionReadFromFileIsCorrect() {
        Mockito.when(testFileNameProvider.getFilename()).thenReturn(CSV_PATH);

        List<Question> questionList = csvQuestionDao.findAll();

        Assertions.assertEquals(QUESTION_LIST_SIZE, questionList.size());
        Assertions.assertTrue(questionList.get(0).text().equals(QUESTION_TEXT));
        Assertions.assertEquals(questionList.get(0).answers().size(), ANSWER_LIST_SIZE);
    }
}