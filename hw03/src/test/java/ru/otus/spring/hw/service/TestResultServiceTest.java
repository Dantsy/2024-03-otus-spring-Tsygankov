package ru.otus.spring.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.hw.config.TestConfig;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

@ExtendWith(MockitoExtension.class)
public class TestResultServiceTest {
    private final Student student = new Student("TestFirstName", "TestLastName");

    @Mock
    private IOService ioService;

    @Mock
    private TestConfig testConfig;

    @Mock
    private LocalizationService localizationService;

    @InjectMocks
    private ResultServiceImpl resultService;

    @Test
    public void shouldShowStudentResult() {
        Mockito.when(testConfig.getRightAnswers()).thenReturn(1);
        Mockito.lenient().when(localizationService.getMessage("test.student", student.getFullName()))
                .thenReturn(String.format("Student: %s", student.getFullName()));

        resultService.showResult(new TestResult(student));

        Mockito.verify(ioService, Mockito.times(1))
                .printFormattedLine(String.format("Student: %s", student.getFullName()));
    }
}