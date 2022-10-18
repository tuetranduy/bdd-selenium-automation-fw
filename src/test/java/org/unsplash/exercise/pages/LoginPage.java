package org.unsplash.exercise.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.utils.PropertyUtils;

public class LoginPage extends BasePage {

    @FindBy(id = "user_email")
    private WebElement emailInput;

    @FindBy(id = "user_password")
    private WebElement passwordInput;

    @FindBy(name = "commit")
    private WebElement loginBtn;

    public void logIn(String userEmail) {
        String password = PropertyUtils.getProperty("user.password");

        LoggingManager.logDebug(getClass(), "user email = " + userEmail);
        setUserEmail(userEmail);
        setPassword(password);
        clickLogin();
    }

    protected void setUserEmail(String userEmailValue) {
        setText(emailInput, userEmailValue, "Unable to set 'Email' text field to '" + userEmailValue + "'");
    }

    protected void setPassword(String passwordValue) {
        setText(passwordInput, passwordValue, "Unable to set 'Password' text field");
    }

    protected void clickLogin() {
        click(loginBtn, "Unable to click 'Login' button");
    }
}
