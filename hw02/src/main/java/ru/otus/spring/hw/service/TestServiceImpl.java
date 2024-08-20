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

    @Override
    public TestResult executeTestFor(Student student) {
        List<Question> questionList = questionDao.findAll();
        TestResult testResult = new TestResult(student);
        ioService.printFormattedLine("Please answer the questions below%n");
        for (Question question : questionList) {
            ioService.printFormattedLine(createQuestionStringBuilder(question));
            testResult.applyAnswer(question, getRightOrNotAnswer(question));
        }
        return testResult;
    }

    private String createQuestionStringBuilder(Question question) {
        StringBuilder questionSb = new StringBuilder();
        questionSb.append("Question: " + question.text() + " %n");
        for (Answer answer : question.answers()) {
            questionSb.append(question.answers().indexOf(answer) + ". " + answer.text() + " %n");
        }
        return questionSb.toString();
    }

    private boolean getRightOrNotAnswer(Question question) {
        int max = question.answers().size() - 1;
        int countAnsw = ioService.readIntForRange(0, max, "Enter int value between 0 - " + max);
        return question.answers().get(countAnsw).isCorrect();
    }
}
