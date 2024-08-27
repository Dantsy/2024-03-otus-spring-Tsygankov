package ru.otus.spring.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppConfig implements TestFileNameProvider,TestConfig {

    @Value("${test.filename}")
    private String testFileName;

    @Value("${test.right.answers.count}")
    private int rightAnswersCountToPass;
}
