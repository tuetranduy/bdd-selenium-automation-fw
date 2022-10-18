package org.unsplash.exercise.tests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.unsplash.exercise.ReportManager;

public class BaseTestNgTest extends AbstractTestNGCucumberTests {

    @BeforeSuite
    protected void setUpSuite(ITestContext testContext) {
        ReportManager.createHtmlReporter();
        ReportManager.createSuiteReport();
        ReportManager.createTest(getSuiteName(testContext));
    }

    @BeforeClass
    public void setUpClass(ITestContext testContext) {
        ReportManager.createNode(getClass().getSimpleName());
    }

    @BeforeTest
    protected void setUpTest(ITestContext testContext) {
        ReportManager.createNode(getTestName(testContext));
    }

    @AfterSuite
    protected void tearDownSuite() {
        ReportManager.generateReport();
    }

    protected String getSuiteName(ITestContext testContext) {
        return testContext.getSuite().getName();
    }

    protected String getTestName(ITestContext testContext) {
        return testContext.getName();
    }
}