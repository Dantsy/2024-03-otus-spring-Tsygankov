package ru.otus.spring.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.dao.dto.QuestionDto;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        return getQuestionsFromCsv(fileNameProvider.getTestFileName())
                .stream()
                .map(qst -> {
                    if (!checkCorrectQuestion(qst)) {
                        throw new QuestionReadException("Not correct question or too little answers");
                    }
                    return qst.toDomainObject();
                }).toList();
    }

    private List<QuestionDto> getQuestionsFromCsv(String filename) throws QuestionReadException {
        try (InputStream inputStream = getFileFromResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException(String.format("Problem reading file or file %s not found",
                    fileNameProvider.getTestFileName()), e);
        }
    }

    private boolean checkCorrectQuestion(QuestionDto qDto) {
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