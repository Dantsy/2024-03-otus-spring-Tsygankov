package ru.otus.spring.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@RequiredArgsConstructor
@ShellComponent
public class Commands {

    @ShellMethod(value = "Start the H2 console to interact with the database.", key = "user")
    public void executeConsole() {
        try {
            Console.main();
        } catch (SQLException e) {
            System.err.println("Failed to start H2 console: " + e.getMessage());
        }
    }
}
