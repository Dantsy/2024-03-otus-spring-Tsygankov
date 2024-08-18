import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.StreamsIOService;
import ru.otus.spring.hw.service.TestService;
import ru.otus.spring.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.List;

public class TestServiceImplTest {
    private static final String Quest_Text = "test1";
    private static final List<Answer> Answer_List =
            Arrays.asList(new Answer("answer1", true), new Answer("answer2", false));
    private static final String Question_String = "Question: test1 %n0. answer1 %n1. answer2 %n %n";
    private IOService ioServiceMock;
    private Question questionMock;
    private CsvQuestionDao csvQuestionDaoMock;

    @BeforeEach
    public void init() {
        ioServiceMock = Mockito.mock(StreamsIOService.class);
        questionMock = new Question(Quest_Text, Answer_List);
        csvQuestionDaoMock = Mockito.mock(CsvQuestionDao.class);
        Mockito.when(csvQuestionDaoMock.findAll()).thenReturn(Arrays.asList(questionMock));
    }

    @Test
    public void testTestServiceImpl() {
        TestService testService = new TestServiceImpl(ioServiceMock, csvQuestionDaoMock);
        testService.executeTest();
        Mockito.verify(ioServiceMock, Mockito.times(1)).printLine("");
        Mockito.verify(ioServiceMock, Mockito.times(2)).printFormattedLine(Mockito.any());
        Mockito.verify(ioServiceMock, Mockito.times(1)).printFormattedLine(Question_String);
    }
}
