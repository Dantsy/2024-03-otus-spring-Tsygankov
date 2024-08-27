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
            ioService.printFormattedLine(createQuestionString(question));
            testResult.applyAnswer(question, getAnswerIsCorrect(question));
        }
        return testResult;
    }

    private String createQuestionString(Question question) {
        StringBuilder questionStringBuilder = new StringBuilder();
        questionStringBuilder.append("Question: " + question.text() + " %n");
        for (int i = 0; i < question.answers().size(); i++) {
            questionStringBuilder.append((i + 1) + ". " + question.answers().get(i).text() + " %n");
        }
        return questionStringBuilder.toString();
    }

    private boolean getAnswerIsCorrect(Question question) {
        int max = question.answers().size();
        int answerIndex = ioService.readIntForRange(1, max, "Enter int value between 1 - " + max) - 1;
        return question.answers().get(answerIndex).isCorrect();
    }
}
