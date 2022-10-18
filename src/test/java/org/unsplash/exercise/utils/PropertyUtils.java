package org.unsplash.exercise.utils;

import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.enums.Browser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyUtils {

    private static final Properties PROPERTIES_FILES = PropertyUtils.loadPropertiesFiles(getPropertiesFiles());

    public static Properties loadPropertiesFiles(String propertiesFiles) {
        Properties properties = new Properties();
        String userDir = PropertyUtils.getProperty("user.dir");
        InputStream inputStream = null;
        String[] allPropertiesFiles = propertiesFiles.split(",");

        for (String propertiesFileName : allPropertiesFiles) {
            propertiesFileName = propertiesFileName.trim();

            try {
                inputStream = Files.newInputStream(Paths.get(userDir + "/src/test/resources/" + propertiesFileName));
                properties.load(inputStream);

            } catch (Exception exception) {
                LoggingManager.logError(PropertyUtils.class, "Unable to load Property Files", exception);

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();

                    } catch (IOException exception) {
                        LoggingManager.logError(PropertyUtils.class, "Unable to close file stream", exception);
                    }
                }
            }
        }

        return properties;
    }

    public static String getProperty(String propertyKey) {
        String propertyValueFromFile = null;

        try {
            propertyValueFromFile = PROPERTIES_FILES.getProperty(propertyKey);

        } catch (Exception exception) {
            LoggingManager.logDebug(PropertyUtils.class, "Property '" + propertyKey + "'" + " does not exist in any of the properties files: '" + getPropertiesFiles() + "'");
        }

        String propertyValueFromSystem = System.getProperty(propertyKey, propertyValueFromFile);
        LoggingManager.logDebug(PropertyUtils.class, propertyKey + " = " + propertyValueFromSystem);
        return propertyValueFromSystem;
    }

    public static String getProperty(String propertyKey, String defaultValue) {
        String propertyValueFromFile = defaultValue;

        try {
            propertyValueFromFile = PROPERTIES_FILES.getProperty(propertyKey, defaultValue);

        } catch (Exception exception) {
            LoggingManager.logDebug(PropertyUtils.class, "Property '" + propertyKey + "'" + " does not exist in any of the properties files: '" + getPropertiesFiles() + "'");
        }

        String propertyValueFromSystem = System.getProperty(propertyKey, propertyValueFromFile);
        LoggingManager.logDebug(PropertyUtils.class, propertyKey + " = " + propertyValueFromSystem);
        return propertyValueFromSystem;
    }

    public static String getBrowserDownloadsDirectory() {
        return getProperty("browser.downloads.dir", "target/downloads");
    }

    public static Browser getBrowser() {
        return Browser.getBrowser(getProperty(Browser.PROPERTY));
    }

    public static Boolean isBrowser(Browser browserExpected) {
        return getBrowser().equals(browserExpected);
    }

    public static String getPropertiesFiles() {
        return System.getProperty(Constants.PROPERTY_PROPERTIES_FILES, Constants.DEFAULT_PROPERTIES_FILES);
    }

}