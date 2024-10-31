package ru.otus.spring.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = TestServiceImpl.class)
@Import(TestServiceImpl.class)
public class TestServiceImplTest {
    private static final String QUESTION_1_FORMATTED_STRING = "Question: test1 %n1. answer1 %n2. answer2 %n";

    @MockBean
    private IOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private TestServiceImpl testService;

    @Test
    public void shouldInvokeIoServiceMethodsWithExpectedArgumentDuringTestExecution() {
        Question question = getQuestion();
        Mockito.when(questionDao.findAll()).thenReturn(List.of(question));
        Mockito.doReturn("Question: test1 %n").when(localizationService).getMessage("test.question", "test1");
        Mockito.doReturn("Enter int value between 0 - 2").when(localizationService).getMessage("test.enter.value.between", "2");
        Mockito.doReturn("Expected message for test.answer.question.below").when(localizationService).getMessage("test.answer.question.below");

        Mockito.doReturn(1).when(ioService).readIntForRange(1, 2, "Enter int value between 0 - 2");

        testService.executeTestFor(getStudent());

        Mockito.verify(ioService, Mockito.times(2)).printFormattedLine(Mockito.any());
        Mockito.verify(ioService, Mockito.times(1)).printFormattedLine("Expected message for test.answer.question.below");
        Mockito.verify(ioService, Mockito.times(1)).printFormattedLine(QUESTION_1_FORMATTED_STRING);
        Mockito.verify(ioService, Mockito.times(1)).readIntForRange(1, 2,
                "Enter int value between 0 - 2");
    }

    private Question getQuestion() {
        return new Question("test1", Arrays.asList(new Answer("answer1", true),
                new Answer("answer2", false)));
    }

    private Student getStudent() {
        return new Student("Name1", "Name2");
    }
}