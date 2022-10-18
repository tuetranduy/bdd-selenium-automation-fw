package org.unsplash.exercise;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.gherkin.model.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {
    private static final String BROWSER = PropertyUtils.getBrowser().fullName;
    private static final String OUTPUT_DIR = "test-output";
    private static final String REPORTS_DIR = "extent-reports";
    private static final String REPORTS_NAME = "Unsplash Test Report";
    private static final String SCREENSHOTS_DIR = "screenshots";
    private static final String USER_DIR = PropertyUtils.getProperty("user.dir");
    private static ExtentSparkReporter reporter;
    private static ExtentReports report;
    private static final ConcurrentHashMap<String, ExtentTest> features = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ExtentTest> scenarios = new ConcurrentHashMap<>();
    private static final ThreadLocal<ExtentTest> currentFeatures = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> currentScenarios = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> currentTests = new ThreadLocal<>();

    public static synchronized ExtentReports getReport() {
        return report;
    }

    public static synchronized void setReport(ExtentReports report) {
        ReportManager.report = report;
    }

    public static ExtentTest getCurrentFeature() {
        return currentFeatures.get();
    }

    public static String getCurrentFeatureName() {
        return getCurrentFeature().getModel().getName();
    }

    public static void setCurrentFeature(ExtentTest feature) {
        currentFeatures.set(feature);
        setCurrentTest(feature);
    }

    public static ExtentTest getCurrentScenario() {
        return currentScenarios.get();
    }

    public static void setCurrentScenario(ExtentTest scenario) {
        currentScenarios.set(scenario);
        setCurrentTest(scenario);
    }

    public static ExtentTest getCurrentTest() {
        return currentTests.get();
    }

    public static void setCurrentTest(ExtentTest test) {
        currentTests.set(test);
    }

    private static String getReportPath() {
        return USER_DIR + File.separator + OUTPUT_DIR + File.separator + REPORTS_DIR + File.separator + REPORTS_NAME + ".html";
    }

    private static void setReportSystemInfo(ExtentReports report) {
        if (report != null) {
            report.setSystemInfo("Browser", BROWSER);
        }
    }

    public static synchronized ExtentSparkReporter createHtmlReporter() {
        if (reporter == null) {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(getReportPath());
            htmlReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            reporter = htmlReporter;
        }

        return reporter;
    }

    public static synchronized ExtentReports createBddReport() {
        if (report == null) {
            ExtentReports bddReport = new ExtentReports();
            bddReport.attachReporter(reporter);
            bddReport.setAnalysisStrategy(AnalysisStrategy.BDD);
            setReportSystemInfo(bddReport);
            setReport(bddReport);
        }

        return report;
    }

    public static synchronized ExtentReports createSuiteReport() {
        if (report == null) {
            ExtentReports suiteReport = new ExtentReports();
            suiteReport.attachReporter(reporter);
            suiteReport.setAnalysisStrategy(AnalysisStrategy.SUITE);
            setReportSystemInfo(suiteReport);
            setReport(suiteReport);
        }

        return report;
    }

    public static synchronized ExtentTest createTest(String name) {
        ExtentTest test = null;
        if (report != null) {
            test = report.createTest(name);
            setCurrentTest(test);
        }

        return test;
    }

    public static synchronized ExtentTest createNode(String name) {
        ExtentTest test = getCurrentTest();
        ExtentTest node = null;
        if (test != null) {
            node = test.createNode(name);
            setCurrentTest(node);
        }

        return node;
    }

    public static synchronized ExtentTest createFeature(String name) {
        ExtentTest feature = features.get(name);
        if (report != null && feature == null) {
            feature = report.createTest(Feature.class, name);
            features.put(name, feature);
        }

        setCurrentFeature(feature);
        setCurrentTest(feature);
        return feature;
    }

    public static synchronized ExtentTest createScenario(String name) {
        ExtentTest feature = getCurrentFeature();
        ExtentTest scenario = scenarios.get(name);
        if (feature != null && scenario == null) {
            scenario = feature.createNode(Scenario.class, name);
            scenarios.put(name, scenario);
        }

        setCurrentScenario(scenario);
        setCurrentTest(scenario);
        return scenario;
    }

    public static ExtentTest createGiven(String step) {
        ExtentTest scenario = getCurrentScenario();
        ExtentTest test = null;
        if (scenario != null) {
            test = scenario.createNode(Given.class, step);
            setCurrentTest(test);
        }

        return test;
    }

    public static ExtentTest createWhen(String step) {
        ExtentTest scenario = getCurrentScenario();
        ExtentTest test = null;
        if (scenario != null) {
            test = scenario.createNode(When.class, step);
            setCurrentTest(test);
        }

        return test;
    }

    public static ExtentTest createThen(String step) {
        ExtentTest scenario = getCurrentScenario();
        ExtentTest test = null;
        if (scenario != null) {
            test = scenario.createNode(Then.class, step);
            setCurrentTest(test);
        }

        return test;
    }

    public static ExtentTest createAnd(String step) {
        ExtentTest scenario = getCurrentScenario();
        ExtentTest test = null;
        if (scenario != null) {
            test = scenario.createNode(And.class, step);
            setCurrentTest(test);
        }

        return test;
    }

    public static void setScenarioTags(Collection<String> tags) {
        ExtentTest scenario = getCurrentScenario();
        if (scenario != null) {
            for (String tag : tags) {
                scenario.assignCategory(tag);
            }
        }
    }

    public static void addScreenshot(File screenshot) {
        try {
            String screenshotPath = SCREENSHOTS_DIR + File.separator + screenshot.getName();
            getCurrentTest().info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

        } catch (Exception exception) {
            LoggingManager.logError(ReportManager.class, "Unable to add screenshot", exception);
        }
    }

    public static synchronized void generateReport() {
        ExtentReports report = getReport();
        if (report != null) {
            report.flush();
        }
    }

    static void reportPass(String message) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            test.pass(message);
        }
    }

    static void reportFail(String message) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            test.fail(message);
        }
    }
}
