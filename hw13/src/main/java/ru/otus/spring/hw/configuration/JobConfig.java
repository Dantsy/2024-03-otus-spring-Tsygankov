package ru.otus.spring.hw.configuration;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.spring.hw.mappers.AuthorMapper;
import ru.otus.spring.hw.mappers.BookMapper;
import ru.otus.spring.hw.mappers.GenreMapper;
import ru.otus.spring.hw.models.documents.AuthorDocument;
import ru.otus.spring.hw.models.documents.BookDocument;
import ru.otus.spring.hw.models.documents.GenreDocument;
import ru.otus.spring.hw.models.entities.Author;
import ru.otus.spring.hw.models.entities.Book;
import ru.otus.spring.hw.models.entities.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory managerFactory;
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Bean
    public Job migrateToMongoJob(Flow authorAndGenreMigrationFlow,
                                 Step bookMigrationStep) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(authorAndGenreMigrationFlow)
                .next(bookMigrationStep)
                .end()
                .build();
    }

    @Bean
    public Flow authorAndGenreMigrationFlow(Flow authorMigrationFlow, Flow genreMigrationFlow) {
        return new FlowBuilder<SimpleFlow>("authorAndGenreMigrationFlow")
                .split(taskJobExecutor())
                .add(authorMigrationFlow, genreMigrationFlow)
                .build();
    }

    @Bean
    public TaskExecutor taskJobExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Flow authorMigrationFlow(Step authorMigrationStep) {
        return new FlowBuilder<SimpleFlow>("authorMigrationFlow")
                .start(authorMigrationStep)
                .build();
    }

    @Bean
    public Step authorMigrationStep(JpaPagingItemReader<Author> authorReader,
                                    ItemProcessor<Author, AuthorDocument> authorProcessor,
                                    MongoItemWriter<AuthorDocument> authorWriter) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, AuthorDocument>chunk(3, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(managerFactory)
                .queryString("select a from Author a")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, AuthorDocument> authorProcessor() {
        return authorMapper::toDocument;
    }

    @StepScope
    @Bean
    public MongoItemWriter<AuthorDocument> authorWriter() {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Flow genreMigrationFlow(Step genreMigrationStep) {
        return new FlowBuilder<SimpleFlow>("genreMigrationFlow")
                .start(genreMigrationStep)
                .build();
    }

    @Bean
    public Step genreMigrationStep(JpaPagingItemReader<Genre> genreReader,
                                   ItemProcessor<Genre, GenreDocument> genreProcessor,
                                   MongoItemWriter<GenreDocument> genreWriter) {
        return new StepBuilder("genreMigrationStep", jobRepository)
                .<Genre, GenreDocument>chunk(3, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(managerFactory)
                .queryString("select g from Genre g")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, GenreDocument> genreProcessor() {
        return genreMapper::toDocument;
    }

    @StepScope
    @Bean
    public MongoItemWriter<GenreDocument> genreWriter() {
        return new MongoItemWriterBuilder<GenreDocument>()
                .collection("genres")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step bookMigrationStep(JpaPagingItemReader<Book> bookReader,
                                  ItemProcessor<Book, BookDocument> bookProcessor,
                                  MongoItemWriter<BookDocument> bookWriter) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, BookDocument>chunk(3, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(managerFactory)
                .queryString("select b from Book b")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, BookDocument> bookProcessor() {
        return bookMapper::toDocument;
    }

    @StepScope
    @Bean
    public MongoItemWriter<BookDocument> bookWriter() {
        return new MongoItemWriterBuilder<BookDocument>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }
}