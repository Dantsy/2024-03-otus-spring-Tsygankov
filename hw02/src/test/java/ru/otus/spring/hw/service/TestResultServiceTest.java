package ru.otus.spring.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.hw.config.AppConfig;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TestResultServiceTest {
    @Mock
    private IOService ioService;
    @Mock
    private AppConfig testConfig;

    @InjectMocks
    private ResultServiceImpl resultService;

    private final Student student = new Student("TestFirstName", "TestLastName");

    @Test
    public void shouldShowStudentResult() {
        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(1);
        TestResult testResult = new TestResult(student);

        resultService.showResult(testResult);

        Mockito.verify(ioService, times(1))
                .printFormattedLine("Student: %s", student.getFullName());
    }
}