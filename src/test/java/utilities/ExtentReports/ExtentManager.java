package utilities.ExtentReports;

import com.relevantcodes.extentreports.ExtentReports;
import utilities.TestUtils;

//OB: ExtentReports extent instance created here. That instance can be reachable by getReporter() method.
public class ExtentManager {

    private static ExtentReports extent;
    public synchronized static ExtentReports getReporter() {
        TestUtils testUtils = new TestUtils();
        if (extent == null) {
            //Set HTML reporting file location
            String[] path = {"test-output","ExtentReportResults.html"};
            extent = new ExtentReports(testUtils.generateFullPath(path), true);
        }
        return extent;
    }
}