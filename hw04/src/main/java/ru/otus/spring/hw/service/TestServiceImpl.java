package ru.otus.spring.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final LocalizationService localizationService;


    @Override
    public TestResult executeTestFor(Student student) {
        List<Question> questionList = questionDao.findAll();
        TestResult testResult = new TestResult(student);
        ioService.printFormattedLine(localizationService.getMessage("test.answer.question.below"));
        for (Question question : questionList) {
            ioService.printFormattedLine(createQuestionString(question));
            testResult.applyAnswer(question, getCorrectAnswer(question));
        }
        return testResult;
    }

    private String createQuestionString(Question question) {
        StringBuilder questionStringBuilder = new StringBuilder();
        questionStringBuilder.append(localizationService.getMessage("test.question", question.text()));
        for (int i = 0; i < question.answers().size(); i++) {
            Answer answer = question.answers().get(i);
            questionStringBuilder.append(i + 1).append(". ").append(answer.text()).append(" %n");
        }
        return questionStringBuilder.toString();
    }

    private boolean getCorrectAnswer(Question question) {
        int max = question.answers().size();
        String messageNotValidInput = localizationService.getMessage("test.enter.value.between",
                String.valueOf(max));
        int countAnswer = ioService.readIntForRange(1, max, messageNotValidInput) - 1;
        return question.answers().get(countAnswer).isCorrect();
    }
}
