package ru.otus.spring.hw.actuators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.hw.services.AuthorService;
import ru.otus.spring.hw.services.BookService;
import ru.otus.spring.hw.services.CommentService;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for DataBaseHealthIndicator")
@SpringBootTest(classes = {DataBaseHealthIndicator.class})
@ActiveProfiles("test")
class DataBaseHealthIndicatorTest {

    @Autowired
    private DataBaseHealthIndicator healthIndicator;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        healthIndicator.setTotalEntitiesChanged(0);
        healthIndicator.setTotalEntitiesAdded(0);
        healthIndicator.setTotalEntitiesDeleted(0);
    }

    @DisplayName("should return status UP if all counters are greater than or equal to 0")
    @Test
    void health_shouldReturnUpStatus() {
        healthIndicator.setTotalEntitiesChanged(1);
        healthIndicator.setTotalEntitiesAdded(1);
        healthIndicator.setTotalEntitiesDeleted(1);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("Number of changed entities: ", 1L);
        assertThat(health.getDetails()).containsEntry("Number of added entities: ", 1L);
        assertThat(health.getDetails()).containsEntry("Number of deleted entities: ", 1L);
    }

    @DisplayName("should return status DOWN if at least one counter is less than 0")
    @Test
    void health_shouldReturnDownStatus() {
        healthIndicator.setTotalEntitiesChanged(-1);
        healthIndicator.setTotalEntitiesAdded(1);
        healthIndicator.setTotalEntitiesDeleted(1);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("Number of changed entities: ", -1L);
        assertThat(health.getDetails()).containsEntry("Number of added entities: ", 1L);
        assertThat(health.getDetails()).containsEntry("Number of deleted entities: ", 1L);
    }

    @DisplayName("should increment the counter of changed entities")
    @Test
    void increaseChangedEntities() {
        healthIndicator.increaseChangedEntities();

        assertThat(healthIndicator.getTotalEntitiesChanged()).isEqualTo(1);
    }

    @DisplayName("should increment the added entity counter")
    @Test
    void increaseAddedEntities() {
        healthIndicator.increaseAddedEntities();

        assertThat(healthIndicator.getTotalEntitiesAdded()).isEqualTo(1);
    }

    @DisplayName("should increment the deleted entity counter")
    @Test
    void increaseDeletedEntities() {
        healthIndicator.increaseDeletedEntities();

        assertThat(healthIndicator.getTotalEntitiesDeleted()).isEqualTo(1);
    }
}