package ru.otus.spring.hw.actuators;

import lombok.Data;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.BookstoreApp;

@Component
@Data
@Aspect
public class DataBaseHealthIndicator implements HealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(BookstoreApp.class);

    private long totalEntitiesChanged;

    private long totalEntitiesAdded;

    private long totalEntitiesDeleted;

    @Override
    public Health health() {
        boolean isDatabaseHealthy = totalEntitiesChanged >= 0 && totalEntitiesAdded >= 0 && totalEntitiesDeleted >= 0;
        Status status = isDatabaseHealthy ? Status.UP : Status.DOWN;

        return Health.status(status)
                .withDetail("Number of changed entities: ", totalEntitiesChanged)
                .withDetail("Number of added entities: ", totalEntitiesAdded)
                .withDetail("Number of deleted entities: ", totalEntitiesDeleted)
                .build();
    }

    public void increaseChangedEntities() {
        totalEntitiesChanged++;
        logger.info("Entity changed");
    }

    public void increaseAddedEntities() {
        totalEntitiesAdded++;
        logger.info("Entity added");
    }

    public void increaseDeletedEntities() {
        totalEntitiesDeleted++;
        logger.info("Entity deleted");
    }

    @Pointcut("execution(* ru.otus.spring.hw.services.AuthorService.insert(..)) ||" +
            "execution(* ru.otus.spring.hw.services.BookService.insert(..)) ||" +
            "execution(* ru.otus.spring.hw.services.CommentService.insert(..))")
    private void insertMethod() {

    }

    @Pointcut("execution(* ru.otus.spring.hw.services.AuthorService.update(..)) ||" +
            "execution(* ru.otus.spring.hw.services.BookService.update(..)) ||" +
            "execution(* ru.otus.spring.hw.services.CommentService.updateById(..))")
    private void updateMethod() {

    }

    @Pointcut("execution(* ru.otus.spring.hw.services.AuthorService.deleteById(..)) ||" +
            "execution(* ru.otus.spring.hw.services.BookService.deleteById(..)) ||" +
            "execution(* ru.otus.spring.hw.services.CommentService.deleteById(..))")
    private void deleteMethod() {

    }

    @AfterReturning("insertMethod()")
    private void countInsertMethodCall() {
        increaseAddedEntities();
    }

    @AfterReturning("updateMethod()")
    private void countUpdateMethodCall() {
        increaseChangedEntities();
    }

    @AfterReturning("deleteMethod()")
    private void countDeleteMethodCall() {
        increaseDeletedEntities();
    }
}
