package ru.otus.spring.hw.actuators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.controllers.GlobalExceptionHandler;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for ExceptionHealthIndicator")
@SpringBootTest(classes = {ExceptionHealthIndicator.class})
@ActiveProfiles("test")
class ExceptionHealthIndicatorTest {

    @Autowired
    private ExceptionHealthIndicator healthIndicator;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        Mockito.reset(globalExceptionHandler);
    }

    @DisplayName("should return status UP if the total number of exceptions is 0")
    @Test
    void health_shouldReturnUpStatus() {
        Mockito.when(globalExceptionHandler.getTotalExceptionsCounter()).thenReturn(0L);
        Mockito.when(globalExceptionHandler.getExceptionNotFoundCounter()).thenReturn(0L);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("Entity not found exceptions number", 0L);
        assertThat(health.getDetails()).containsEntry("Total exceptions number", 0L);
    }

    @DisplayName("should return status DOWN if the total number of exceptions is greater than 0")
    @Test
    void health_shouldReturnDownStatus() {
        Mockito.when(globalExceptionHandler.getTotalExceptionsCounter()).thenReturn(1L);
        Mockito.when(globalExceptionHandler.getExceptionNotFoundCounter()).thenReturn(1L);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("Entity not found exceptions number", 1L);
        assertThat(health.getDetails()).containsEntry("Total exceptions number", 1L);
    }
}