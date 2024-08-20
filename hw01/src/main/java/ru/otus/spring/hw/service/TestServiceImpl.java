package ru.otus.spring.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        List<Question> questionsList = questionDao.findAll();
        ioService.printFormattedLine("Please answer the questions below%n");
        ioService.printFormattedLine(createQuestionString(questionsList));
        ioService.printLine("");
    }

    private String createQuestionString(List<Question> questionsList) {
        StringBuilder questionSb = new StringBuilder();
        for (Question question : questionsList) {
            questionSb.append("Question: ").append(question.text()).append(" %n");
            for (Answer answer : question.answers()) {
                questionSb.append(question.answers().indexOf(answer)).append(". ").append(answer.text()).append(" %n");
            }
            questionSb.append(" %n");
        }
        return questionSb.toString();
    }
}