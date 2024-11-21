package ru.otus.spring.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.controllers.GlobalExceptionHandler;

@Component
@RequiredArgsConstructor
public class ExceptionHealthIndicator implements HealthIndicator {

    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    public Health health() {
        Status status = globalExceptionHandler.getTotalExceptionsCounter() == 0 ? Status.UP : Status.DOWN;

        return Health.status(status)
                .withDetail("Entity not found exceptions number", globalExceptionHandler.getExceptionNotFoundCounter())
                .withDetail("Total exceptions number", globalExceptionHandler.getTotalExceptionsCounter())
                .build();
    }
}