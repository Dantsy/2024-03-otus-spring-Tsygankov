package ru.otus.spring.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.config.AppConfig;
import ru.otus.spring.hw.config.TestConfig;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

import java.util.List;

import static org.mockito.Mockito.times;

public class TestResultServiceTest {
    private IOService ioService;
    private TestConfig testConfig;
    private final Student student = new Student("TestFirstName", "TestLastName");

    @BeforeEach
    public void init() {
        ioService = Mockito.mock(StreamsIOService.class);
        testConfig = Mockito.mock(AppConfig.class);
        Question question = new Question("test1", List.of(new Answer("answer1", true),
                new Answer("answer2", false)));
        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(1);
    }

    @Test
    public void shouldShowStudentResult() {
        ResultService resultService = new ResultServiceImpl(testConfig, ioService);
        TestResult testResult = new TestResult(student);
        resultService.showResult(testResult);

        Mockito.verify(ioService, times(1))
                .printFormattedLine("Student: %s", student.getFullName());
    }
}
