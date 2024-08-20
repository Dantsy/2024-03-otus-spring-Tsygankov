package ru.otus.spring.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

import java.util.List;

public class TestServiceImplTest {
    private static final String Question_1_string = "Question: test1 %n0. answer1 %n1. answer2 %n";
    private IOService ioService;
    private QuestionDao questionDao;
    private final Student student = new Student("TestFirstName", "TestLastName");

    @BeforeEach
    public void init() {
        ioService = Mockito.mock(StreamsIOService.class);
        Question question = new Question("test1", List.of(new Answer("answer1", true),
                new Answer("answer2", false)));
        questionDao = Mockito.mock(CsvQuestionDao.class);
        Mockito.when(questionDao.findAll()).thenReturn(List.of(question));
    }

    @Test
    public void shouldInvokeIoServiceMethodsWithExpectedArgumentDuringTestExecution() {
        TestService testService = new TestServiceImpl(ioService, questionDao);
        testService.executeTestFor(student);
        Mockito.verify(ioService, Mockito.times(2)).printFormattedLine(Mockito.any());
        Mockito.verify(ioService, Mockito.times(1)).printFormattedLine(Question_1_string);
        Mockito.verify(ioService, Mockito.times(1)).readIntForRange(0, 1,
                "Enter int value between 0 - 1");
    }
}