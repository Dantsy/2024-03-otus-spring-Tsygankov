package ru.otus.spring.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.dao.dto.QuestionDto;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        List<QuestionDto> questionDtos = getQuestionsFromCsv(fileNameProvider.getFilename());
        List<Question> allQuestionsList = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {
            if (!checkCorrectLine(questionDto)) {
                throw new QuestionReadException("Not correct question or too little answers");
            }
            allQuestionsList.add(questionDto.toDomainObject());
        }
        return allQuestionsList;
    }

    private List<QuestionDto> getQuestionsFromCsv(String filename) throws QuestionReadException {
        try (InputStream inputStream = getFileFromResourceAsStream(fileNameProvider.getFilename());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException(String.format("Problem read file question or File %s not found",
                    fileNameProvider.getFilename()), e);
        }
    }

    private boolean checkCorrectLine(QuestionDto qDto) {
        return ((qDto.getText().length() > 3) && (qDto.getAnswers().size() > 2));
    }

    private InputStream getFileFromResourceAsStream(String filename) throws QuestionReadException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new QuestionReadException(String.format("File %s not found", filename));
        } else {
            return inputStream;
        }
    }
}