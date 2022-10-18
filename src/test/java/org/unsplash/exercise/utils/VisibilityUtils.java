package org.unsplash.exercise.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;

public class VisibilityUtils {

    public static void waitUntilVisible(Class<?> className, WebElement element, String errorMessage) {
        try {
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(element));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void waitUntilVisible(Class<?> className, By locator, String errorMessage) {
        try {
            WebElement element = WebDriverManager.getWebDriver().findElement(locator);
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(element));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }
}
