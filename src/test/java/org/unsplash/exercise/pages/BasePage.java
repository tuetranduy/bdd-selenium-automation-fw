package org.unsplash.exercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;
import org.unsplash.exercise.utils.*;

public class BasePage {
    public BasePage() {
        initializePageElements();
    }

    private void initializePageElements() {
        PageFactory.initElements(WebDriverManager.getWebDriver(), this);
    }

    public BasePage loadPage() {
        String loginUrl = PropertyUtils.getProperty("url") + "login/";

        LoggingManager.logDebug(getClass(), "URL = " + loginUrl);

        WebDriverManager.loadUrl(loginUrl);
        return this;
    }

    public BasePage click(WebElement element, String errorMessage) {
        waitUntilVisible(element, errorMessage);
        waitUntilClickable(element, errorMessage);
        ClickUtils.click(getClass(), element, errorMessage);
        return this;
    }

    public BasePage click(By locator, String errorMessage) {
        waitUntilVisible(locator, errorMessage);
        waitUntilClickable(locator, errorMessage);
        ClickUtils.click(getClass(), locator, errorMessage);
        return this;
    }

    public BasePage setText(WebElement element, String textValue, String errorMessage) {
        clearText(element, errorMessage);
        TextUtils.setText(getClass(), element, textValue, errorMessage);
        waitUntilTextEquals(element, textValue, errorMessage);
        return this;
    }

    public String getText(WebElement textElement, String errorMessage) {
        waitUntilVisible(textElement, errorMessage);
        return TextUtils.getText(getClass(), textElement, errorMessage);
    }

    public BasePage clearText(WebElement textElement, String errorMessage) {
        waitUntilVisible(textElement, errorMessage);
        TextUtils.clearText(getClass(), textElement, errorMessage);
        return this;
    }

    public BasePage hoverOver(WebElement element, String errorMessage) {
        waitUntilVisible(element, errorMessage);
        MouseUtils.hoverOver(getClass(), element, errorMessage);
        return this;
    }

    public String getAttributeValue(WebElement element, String attributeName, String errorMessage) {
        waitUntilVisible(element, errorMessage);
        return TextUtils.getAttributeValue(getClass(), element, attributeName, errorMessage);
    }

    public void waitUntilVisible(WebElement element, String errorMessage) {
        VisibilityUtils.waitUntilVisible(getClass(), element, errorMessage);
    }

    public void waitUntilVisible(By locator, String errorMessage) {
        VisibilityUtils.waitUntilVisible(getClass(), locator, errorMessage);
    }

    public BasePage waitUntilClickable(WebElement element, String errorMessage) {
        ClickUtils.waitUntilClickable(getClass(), element, errorMessage);
        return this;
    }

    public BasePage waitUntilClickable(By locator, String errorMessage) {
        ClickUtils.waitUntilClickable(getClass(), locator, errorMessage);
        return this;
    }

    public BasePage waitUntilTextEquals(WebElement textElement, String textValue, String errorMessage) {
        TextUtils.waitUntilTextEquals(getClass(), textElement, textValue, true, errorMessage);
        return this;
    }

    public BasePage refresh() {
        WebDriverManager.getWebDriver().navigate().refresh();

        return this;
    }

    public BasePage sendKeys(Keys keys) {
        new Actions(WebDriverManager.getWebDriver()).sendKeys(keys).perform();

        return this;
    }
}
