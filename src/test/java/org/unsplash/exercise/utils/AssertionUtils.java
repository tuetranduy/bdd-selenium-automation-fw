package org.unsplash.exercise.utils;

import org.unsplash.exercise.LoggingManager;

public class AssertionUtils {

    public static void assertTrue(Class<?> className, Boolean condition, String message) {
        if (condition) {
            LoggingManager.logPass(className, message);
        } else {
            LoggingManager.logFail(className, message);
        }
    }
}