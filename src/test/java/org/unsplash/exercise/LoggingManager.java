package org.unsplash.exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;

public class LoggingManager {

    private static Logger getLogger(Class<?> className) {
        return LoggerFactory.getLogger(className);
    }

    public static void logDebug(Class<?> className, String message) {
        getLogger(className).debug(message);
    }

    public static void logInfo(Class<?> className, String message) {
        getLogger(className).info(message);
    }

    public static void logError(Class<?> className, String message, Exception exception) {
        getLogger(className).error(message, exception);
        ReportManager.reportFail(message);
        Assert.fail(message, exception);
    }

    public static void logPass(Class<?> className, String message) {
        getLogger(className).info(message);
        ReportManager.reportPass(message);
    }

    public static void logFail(Class<?> className, String message) {
        getLogger(className).error(message);
        ReportManager.reportFail(message);
        Assert.fail(message);
    }

    public static void logGiven(Class<?> className, String step) {
        String message = "Given " + step;
        getLogger(className).info(message);
        ReportManager.createGiven(message);
    }

    public static void logWhen(Class<?> className, String step) {
        String message = "When " + step;
        getLogger(className).info(message);
        ReportManager.createWhen(message);
    }

    public static void logThen(Class<?> className, String step) {
        String message = "Then " + step;
        getLogger(className).info(message);
        ReportManager.createThen(message);
    }

    public static void logAnd(Class<?> className, String step) {
        String message = "And " + step;
        getLogger(className).info(message);
        ReportManager.createAnd(message);
    }

    public static void addScreenshot(File screenshot) {
        ReportManager.addScreenshot(screenshot);
    }

}
