package ru.otus.spring.hw.security;

public interface LoginContext {

    void login(String username);

    boolean isUserLoggedIn();

    String getLogin();

}
