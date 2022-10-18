package org.unsplash.exercise.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;

public class ClickUtils {

    public static void click(Class<?> className, By locator, String errorMessage) {
        try {
            WebElement element = WebDriverManager.getWebDriver().findElement(locator);
            element.click();

        } catch (Exception exception) {
            clickUsingJavaScript(className, locator, errorMessage);
        }
    }

    public static void click(Class<?> className, WebElement element, String errorMessage) {
        try {
            element.click();

        } catch (Exception exception) {
            clickUsingJavaScript(className, element, errorMessage);
        }
    }

    public static void clickUsingJavaScript(Class<?> className, WebElement element, String errorMessage) {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverManager.getWebDriver();
            jsExecutor.executeScript("arguments[0].click()", element);

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void clickUsingJavaScript(Class<?> className, By locator, String errorMessage) {
        try {
            WebElement element = WebDriverManager.getWebDriver().findElement(locator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverManager.getWebDriver();
            jsExecutor.executeScript("arguments[0].click()", element);

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void waitUntilClickable(Class<?> className, WebElement element, String errorMessage) {
        try {
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void waitUntilClickable(Class<?> className, By locator, String errorMessage) {
        try {
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(locator));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

}
