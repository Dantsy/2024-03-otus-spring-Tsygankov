package ru.otus.spring.hw.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobServiceImpl implements JobService {

    @Setter
    @Getter
    private Long executionId;

    private final JobOperator jobOperator;

    @Override
    public void run() throws Exception {
        if (executionId != null) {
            throw new IllegalArgumentException("The task has already started");
        }

        Properties properties = new Properties();
        executionId = jobOperator.start("importLibraryJob", properties);
        log.info("Task \"{}\" completed", jobOperator.getSummary(executionId));
    }

    @Override
    public String showJobInfo() throws Exception {
        if (executionId == null) {
            throw new IllegalStateException("Job has not been started yet");
        }
        return jobOperator.getSummary(executionId);
    }
}