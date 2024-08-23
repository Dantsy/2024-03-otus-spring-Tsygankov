package ru.otus.spring.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {
    private static final String QUESTION_1_FORMATTED_STRING = "Question: test1 %n1. answer1 %n2. answer2 %n";

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    private final Student student = new Student("TestFirstName", "TestLastName");

    @Test
    public void shouldInvokeIoServiceMethodsWithExpectedArgumentDuringTestExecution() {
        Question question = new Question("test1", List.of(new Answer("answer1", true),
                new Answer("answer2", false)));
        Mockito.when(questionDao.findAll()).thenReturn(List.of(question));

        Mockito.when(ioService.readIntForRange(1, 2, "Enter int value between 1 - 2")).thenReturn(1);

        testService.executeTestFor(student);

        Mockito.verify(ioService, Mockito.times(2)).printFormattedLine(Mockito.any());
        Mockito.verify(ioService, Mockito.times(1)).printFormattedLine(QUESTION_1_FORMATTED_STRING);
        Mockito.verify(ioService, Mockito.times(1)).readIntForRange(1, 2, "Enter int value between 1 - 2");
    }
}