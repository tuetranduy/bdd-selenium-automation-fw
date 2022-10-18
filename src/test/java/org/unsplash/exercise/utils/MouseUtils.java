package org.unsplash.exercise.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;

public class MouseUtils {
    public static void hoverOver(Class<?> className, WebElement element, String errorMessage) {
        try {
            Actions actions = new Actions(WebDriverManager.getWebDriver());
            actions.moveToElement(element).perform();

        } catch (Exception exception) {
            LoggingManager.logError(className, errorMessage, exception);
        }
    }

}
