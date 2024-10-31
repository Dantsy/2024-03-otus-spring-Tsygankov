package ru.otus.spring.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.hw.services.JobService;

@RequiredArgsConstructor
@ShellComponent
public class MigrationCommands {

    private final JobService jobService;

    @ShellMethod(value = "Start migration with clear", key = "start")
    public void startMigrationJobWithClear() {
        try {
            jobService.run();
            System.out.println("Migration job started successfully.");
        } catch (Exception e) {
            System.err.println("Failed to start migration job: " + e.getMessage());
        }
    }

    @ShellMethod(value = "Show information about the job", key = "info")
    public String showJobInfo() {
        try {
            String jobInfo = jobService.showJobInfo();
            System.out.println("Job info retrieved successfully.");
            return jobInfo;
        } catch (Exception e) {
            System.err.println("Failed to retrieve job info: " + e.getMessage());
            return "Failed to retrieve job info.";
        }
    }
}