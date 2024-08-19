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
        for (Question qst : questionsList) {
            questionSb.append("Question: ").append(qst.text()).append(" %n");
            for (Answer answ : qst.answers()) {
                questionSb.append(qst.answers().indexOf(answ)).append(". ").append(answ.text()).append(" %n");
            }
            questionSb.append(" %n");
        }
        return questionSb.toString();
    }
}