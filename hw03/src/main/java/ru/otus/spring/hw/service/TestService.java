package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

public interface TestService {

    TestResult executeTestFor(Student student);
}
