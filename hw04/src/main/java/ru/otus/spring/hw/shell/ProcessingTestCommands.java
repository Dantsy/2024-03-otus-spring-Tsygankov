package ru.otus.spring.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.hw.security.LoginContext;
import ru.otus.spring.hw.service.TestRunnerService;

@ShellComponent(value = "Processing Test Commands")
@RequiredArgsConstructor
public class ProcessingTestCommands {

    private final TestRunnerService testRunnerService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Login command", key = {"l","login"})
    public String login(@ShellOption(defaultValue = "Anonymous") String userName) {
        loginContext.login(userName);
        return String.format("Welcome: %s!", userName);
    }

    @ShellMethod(value = "Show login command", key = {"w","who"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public String whoIAm() {
        return String.format("You are logged in as: %s",loginContext.getLogin());
    }

    @ShellMethod(value = "Begin student test", key = {"tst","test"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public void startTesting() {
        testRunnerService.run();
    }

    private Availability isTestCommandAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Login first");
    }
}
