package org.unsplash.exercise;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.SessionId;
import org.unsplash.exercise.enums.Browser;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WebDriverManager {
    private static final ThreadLocal<WebDriver> webDrivers = new ThreadLocal<>();

    public static synchronized WebDriver getWebDriver() {
        return webDrivers.get();
    }

    public static synchronized void setWebDriver(WebDriver webDriver) {
        webDrivers.set(webDriver);
    }

    public static synchronized Boolean doesDriverExist() {
        return getWebDriver() != null;
    }

    public static synchronized Boolean isDriverSessionActive() {
        return getWebDriverSessionId() != null;
    }

    private static synchronized SessionId getWebDriverSessionId() {
        SessionId sessionId = null;

        if (doesDriverExist()) {
            if (PropertyUtils.isBrowser(Browser.CHROME)) {
                sessionId = ((ChromeDriver) WebDriverManager.getWebDriver()).getSessionId();

            } else if (PropertyUtils.isBrowser(Browser.EDGE)) {
                sessionId = ((EdgeDriver) WebDriverManager.getWebDriver()).getSessionId();

            } else if (PropertyUtils.isBrowser(Browser.FIREFOX)) {
                sessionId = ((FirefoxDriver) WebDriverManager.getWebDriver()).getSessionId();

            } else {
                throw new RuntimeException("The '" + Browser.PROPERTY + "' property value of '" + PropertyUtils.getBrowser() + "' is not recognized");
            }
        }

        return sessionId;
    }

    public static WebDriver openNewBrowser() {
        WebDriver webDriver = null;

        if (PropertyUtils.isBrowser(Browser.CHROME)) {
            webDriver = openNewChromeBrowser();
        }

        return webDriver;
    }

    public static WebDriver openNewChromeBrowser() {
        ChromeOptions chromeOptions = new ChromeOptions();
        return openNewChromeBrowser(chromeOptions);
    }

    public static WebDriver openNewChromeBrowser(ChromeOptions chromeOptions) {
        LoggingManager.logDebug(WebDriverManager.class, Browser.PROPERTY + " = " + Browser.CHROME.fullName);

        WebDriver chromeDriver;

        try {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        } catch (Exception exception) {
            LoggingManager.logError(WebDriverManager.class, "Unable to setup the Chrome driver", exception);
        }

        chromeOptions.addArguments("--start-maximized");
        chromeOptions.setAcceptInsecureCerts(true);

        HashMap<String, Object> chromePreferences = new HashMap<>();
        chromePreferences.put("download.default_directory", getBrowserDownloadsDirectory());
        chromePreferences.put("download.prompt_for_download", false);

        chromeOptions.setExperimentalOption("prefs", chromePreferences);

        chromeDriver = new ChromeDriver(chromeOptions);

        setWebDriver(chromeDriver);
        return chromeDriver;
    }

    public static void loadUrl(String url) {
        WebDriver webDriver = getWebDriver();
        if (webDriver != null) {
            webDriver.get(url);
        }
    }

    public static File getScreenshot() {
        File screenshotFile;

        try {
            WebDriver webDriver = getWebDriver();
            File screenshotSource = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

            String screenshotFilePath = PropertyUtils.getProperty("user.dir") + File.separator;
            screenshotFilePath += "test-output" + File.separator;
            screenshotFilePath += "extent-reports" + File.separator;
            screenshotFilePath += "screenshots" + File.separator;

            String reportName = "Unsplash Test Report";
            screenshotFilePath += reportName + "_";
            screenshotFilePath += System.currentTimeMillis() + ".png";

            screenshotFile = new File(screenshotFilePath);
            org.apache.commons.io.FileUtils.copyFile(screenshotSource, screenshotFile);

        } catch (IOException exception) {
            throw new RuntimeException("Unable to take screenshot", exception);
        }

        return screenshotFile;
    }

    public static String getBrowserDownloadsDirectory() {
        String downloadsDirectory = PropertyUtils.getBrowserDownloadsDirectory().replace("/", File.separator).replace("\\", File.separator);
        String downloadsDirectoryFilePath = PropertyUtils.getProperty("user.dir") + File.separator + downloadsDirectory;
        File downloadsDirectoryFile = new File(downloadsDirectoryFilePath);
        if (!downloadsDirectoryFile.exists()) {
            downloadsDirectoryFile.mkdirs();
        }

        LoggingManager.logDebug(WebDriverManager.class, "browser.downloads.dir" + " = " + downloadsDirectoryFilePath);
        return downloadsDirectoryFilePath;
    }

    public static void closeBrowser() {
        WebDriver webDriver = getWebDriver();
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
