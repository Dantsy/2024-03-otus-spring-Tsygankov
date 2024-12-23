package ru.otus.spring.hw.commandlinerunners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.service.TestRunnerService;

@Component
public class AppLauncher implements CommandLineRunner {
    private final TestRunnerService testRunnerService;

    public AppLauncher(TestRunnerService testRunnerService) {
        this.testRunnerService = testRunnerService;
    }

    @Override
    public void run(String... args) throws Exception {
        testRunnerService.run();
    }
}
