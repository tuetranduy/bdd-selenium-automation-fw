package org.unsplash.exercise.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;

public class TextUtils {

    public static void clearText(Class<?> className, By textLocator, String errorMessage) {
        try {
            WebElement textElement = WebDriverManager.getWebDriver().findElement(textLocator);
            textElement.clear();

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void clearText(Class<?> className, WebElement textElement, String errorMessage) {
        try {
            textElement.clear();

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void setText(Class<?> className, WebElement textElement, String textValue, String errorMessage) {
        try {
            textElement.sendKeys(textValue);

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static String getText(Class<?> className, WebElement textElement, String errorMessage) {
        String text = null;

        try {
            text = textElement.getText();

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }

        return text;
    }

    public static String getAttributeValue(Class<?> className, WebElement element, String attributeName, String errorMessage) {
        String attributeValue = null;

        try {
            attributeValue = element.getAttribute(attributeName);

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }

        return attributeValue;
    }

    public static void waitUntilTextEquals(Class<?> className, WebElement textElement, String textValue, Boolean caseSensitive, String errorMessage) {
        try {
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.or(
                    textToBe(textElement, textValue, caseSensitive),
                    valueToBe(textElement, textValue, caseSensitive)
            ));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static void waitUntilTextEquals(Class<?> className, By textLocator, String textValue, Boolean caseSensitive, String errorMessage) {
        try {
            WaitUtils.waitDefault().ignoring(StaleElementReferenceException.class).until(ExpectedConditions.or(
                    textToBe(textLocator, textValue, caseSensitive),
                    valueToBe(textLocator, textValue, caseSensitive)
            ));

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

    public static ExpectedCondition<Boolean> textToBe(By textLocator, String textValue, Boolean caseSensitive) {
        return (WebDriver driver) -> {
            WebElement textElement = WebDriverManager.getWebDriver().findElement(textLocator);
            return doesTextEqual(textElement, textValue, caseSensitive);
        };
    }

    public static ExpectedCondition<Boolean> textToBe(WebElement textElement, String textValue, Boolean caseSensitive) {
        return (WebDriver driver) -> doesTextEqual(textElement, textValue, caseSensitive);
    }

    public static ExpectedCondition<Boolean> valueToBe(WebElement textElement, String textValue, Boolean caseSensitive) {
        return (WebDriver driver) -> doesValueEqual(textElement, textValue, caseSensitive);
    }

    public static ExpectedCondition<Boolean> valueToBe(By textLocator, String textValue, Boolean caseSensitive) {
        return (WebDriver driver) -> {
            WebElement textElement = WebDriverManager.getWebDriver().findElement(textLocator);
            return doesValueEqual(textElement, textValue, caseSensitive);
        };
    }

    private static boolean doesTextEqual(WebElement textElement, String textValue, Boolean caseSensitive) {
        if (caseSensitive) {
            return textElement.getText().equals(textValue);
        } else {
            return textElement.getText().equalsIgnoreCase(textValue);
        }
    }

    private static boolean doesValueEqual(WebElement textElement, String textValue, Boolean caseSensitive) {
        if (caseSensitive) {
            return textElement.getAttribute("value").equals(textValue);
        } else {
            return textElement.getAttribute("value").equalsIgnoreCase(textValue);
        }
    }
}
