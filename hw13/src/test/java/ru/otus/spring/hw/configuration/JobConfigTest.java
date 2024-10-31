package ru.otus.spring.hw.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.hw.models.documents.AuthorDocument;
import ru.otus.spring.hw.models.documents.BookDocument;
import ru.otus.spring.hw.models.documents.GenreDocument;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {
    private static final String JOB_NAME = "importLibraryJob";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
        mongoTemplate.dropCollection(AuthorDocument.class);
        mongoTemplate.dropCollection(BookDocument.class);
        mongoTemplate.dropCollection(GenreDocument.class);
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);

        JobParameters parameters = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        assertEquals(3, mongoTemplate.count(new Query(), BookDocument.class));
        assertEquals(3, mongoTemplate.count(new Query(), AuthorDocument.class));
        assertEquals(6, mongoTemplate.count(new Query(), GenreDocument.class));

        AuthorDocument authorDocument = mongoTemplate.findOne(new Query(), AuthorDocument.class);
        assertThat(authorDocument).isNotNull();
        assertThat(authorDocument.getFullName()).isNotBlank();

        BookDocument bookDocument = mongoTemplate.findOne(new Query(), BookDocument.class);
        assertThat(bookDocument).isNotNull();
        assertThat(bookDocument.getTitle()).isNotBlank();
        assertThat(bookDocument.getAuthor()).isNotNull();
        assertThat(bookDocument.getGenres()).isNotEmpty();

        GenreDocument genreDocument = mongoTemplate.findOne(new Query(), GenreDocument.class);
        assertThat(genreDocument).isNotNull();
        assertThat(genreDocument.getName()).isNotBlank();
    }
}