package listener;

import base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import utilities.ExtentReports.ExtentManager;
import utilities.ExtentReports.ExtentTestManager;

import java.io.IOException;

public class Listener extends BaseTest implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        try {
            if (!testUtils.isTestRunnable(getTestMethodName(iTestResult))){
                log.info("Skipping the test: "+ getTestMethodName(iTestResult)+" because is not approved to run");
              throw new SkipException("Skipping the test: "+ getTestMethodName(iTestResult)+" because is not approved to run");
            }else {
                ExtentTestManager.startTest(getTestMethodName(iTestResult),iTestResult.getMethod().getDescription());
                ExtentTestManager.getTest().log(LogStatus.INFO, "Test "+ getTestMethodName(iTestResult)+" started with out validation from suite");
            }
        } catch (IOException e) {
            ExtentTestManager.getTest().log(LogStatus.ERROR, "Error encountered on test suite validation: "+e.getMessage());
            log.info("ERROR: "+ getTestMethodName(iTestResult)+" error encountered on test suite validation");
            log.info(e.getMessage());
        }
      /*
        ExtentTestManager.startTest(getTestMethodName(iTestResult),iTestResult.getMethod().getDescription());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Test "+ getTestMethodName(iTestResult)+" started with out validation from suite");
       */
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test "+ getTestMethodName(iTestResult)+" passed");
        log.info("Test "+ getTestMethodName(iTestResult)+" passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("Test "+ getTestMethodName(iTestResult)+" failed");
        log.info("ERROR: "+iTestResult.getThrowable().toString());
        ExtentTestManager.getTest().log(LogStatus.ERROR, "Test "+ getTestMethodName(iTestResult)+" failed: "+
                iTestResult.getThrowable().toString());
        //Take base64Screenshot screenshot.
        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).
                getScreenshotAs(OutputType.BASE64);
        //ExtentReports log and screenshot operations for failed tests.
        ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed",
                ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test: "+getTestMethodName(iTestResult)+" Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        iTestContext.setAttribute("WebDriver", driver);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }


    private static String getTestMethodName(ITestResult iTestResult){
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
}
