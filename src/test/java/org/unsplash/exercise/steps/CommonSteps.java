package org.unsplash.exercise.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import org.testng.Reporter;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.ReportManager;
import org.unsplash.exercise.WebDriverManager;

import java.util.Collection;

public class CommonSteps {

    @Before
    public static void setUp(Scenario scenario) {
        setUpScenario(scenario);
        openBrowser();
    }

    @After
    public static void tearDown(Scenario scenario) {
        tearDownUi(scenario);
        tearDownScenario(scenario);
        closeBrowser();
    }

    public static void openBrowser() {
        if (!WebDriverManager.isDriverSessionActive()) {
            WebDriverManager.openNewBrowser();
            LoggingManager.logInfo(CommonSteps.class, "Opened a new browser window");
        }
    }

    public static void closeBrowser() {
        if (WebDriverManager.isDriverSessionActive()) {
            WebDriverManager.closeBrowser();
            LoggingManager.logInfo(CommonSteps.class, "Closed the browser window");
        }
    }

    public static void tearDownUi(Scenario scenario) {
        addScreenshotToScenario(scenario);
        closeBrowserIfScenarioFailed(scenario);
    }

    private static void addScreenshotToScenario(Scenario scenario) {
        if (scenarioPassed(scenario) || scenarioFailed(scenario)) {
            if (WebDriverManager.isDriverSessionActive()) {
                LoggingManager.addScreenshot(WebDriverManager.getScreenshot());
            }
        }
    }

    private static void closeBrowserIfScenarioFailed(Scenario scenario) {
        if (scenarioFailed(scenario)) {
            closeBrowser();
        }
    }

    protected static void setUpScenario(Scenario scenario) {
        logFeatureAndScenario(scenario);
        setScenarioTags(scenario);
    }

    protected static void tearDownScenario(Scenario scenario) {
        logScenarioStatus(scenario);
    }

    private static Collection<String> getScenarioTags(Scenario scenario) {
        return scenario.getSourceTagNames();
    }

    private static void setScenarioTags(Scenario scenario) {
        ReportManager.setScenarioTags(getScenarioTags(scenario));
    }

    private static String getCurrentFeatureName() {
        String featureName = null;

        try {
            Object[] currentTestParameters = Reporter.getCurrentTestResult().getParameters();
            featureName = currentTestParameters[1].toString().replaceAll("(^\"Optional\\[|\\]\"$)", "");

        } catch (Exception exception) {
            LoggingManager.logError(CommonSteps.class, "Unable to parse feature (check for syntax errors)", exception);
        }

        return featureName;
    }

    private static void logFeatureAndScenario(Scenario scenario) {
        String featureName = getCurrentFeatureName();
        String scenarioName = scenario.getName();
        LoggingManager.logDebug(CommonSteps.class, featureName + " > " + scenarioName);

        ReportManager.createFeature(featureName);
        ReportManager.createScenario(scenarioName);
    }

    private static void logScenarioStatus(Scenario scenario) {
        String featureName = ReportManager.getCurrentFeatureName();
        String scenarioName = scenario.getName();
        String scenarioStatus = scenario.getStatus().toString();

        if (scenarioPassed(scenario)) {
            LoggingManager.logDebug(CommonSteps.class, featureName + " > " + scenarioName + " > " + scenarioStatus);

        } else if (scenarioFailed(scenario)) {
            LoggingManager.logFail(CommonSteps.class, featureName + " > " + scenarioName + " > " + scenarioStatus);

        }
    }

    protected static Boolean scenarioPassed(Scenario scenario) {
        return scenario.getStatus() == Status.PASSED;
    }

    protected static Boolean scenarioFailed(Scenario scenario) {
        return scenario.getStatus() == Status.FAILED;
    }

}
