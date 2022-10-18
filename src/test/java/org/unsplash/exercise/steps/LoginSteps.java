package org.unsplash.exercise.steps;

import io.cucumber.java.en.Given;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.pages.LoginPage;

public class LoginSteps {
    @Given("^I log in with account '(.*)'$")
    public void userLoginByAccount(String userEmail) {
        LoggingManager.logGiven(getClass(), "I log in with account " + userEmail);

        LoginPage loginPage = new LoginPage();

        loginPage.loadPage();
        loginPage.logIn(userEmail);
    }
}
