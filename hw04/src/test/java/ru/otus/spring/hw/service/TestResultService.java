package ru.otus.spring.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.hw.config.TestConfig;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

@SpringBootTest(classes = ResultServiceImpl.class)
public class TestResultService {
    private static final String Student_name = "TestFirstName TestLastName";

    @MockBean
    private IOService ioService;

    @MockBean
    private TestConfig testConfig;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private ResultService resultService;

    @BeforeEach
    public void init() {
        Mockito.when(testConfig.getRightAnswers()).thenReturn(1);
        Mockito.when(localizationService.getMessage("test.student", Student_name)).
                thenReturn(String.format("Student: %s", Student_name));
    }

    @Test
    public void shouldInvokeIoServiceMethodsWithExpectedArgumentDuringTestExecution() {
        resultService.showResult(getTestResult());
        Mockito.verify(ioService, Mockito.times(1))
                .printFormattedLine(String.format("Student: %s", Student_name));
    }

    private TestResult getTestResult() {
        TestResult testresult = Mockito.mock(TestResult.class);
        Mockito.when(testresult.getStudent()).thenReturn(new Student(Student_name.split(" ")[0],
                Student_name.split(" ")[1]));
        return testresult;
    }
}