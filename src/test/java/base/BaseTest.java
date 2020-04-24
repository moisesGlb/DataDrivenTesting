package base;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utilities.ExtentReports.ExtentTestManager;
import utilities.TestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public static WebDriver driver;
    public static Properties configProperties;
    public static Properties ORProperties;
    public static FileInputStream fis;
    public static WebDriverWait wait;
    public static Logger log = Logger.getLogger("devpinoyLogger");
    protected static TestUtils testUtils = new TestUtils();
    private String[] path;
    @BeforeClass
    @Parameters({"browser"})
    public void SetUp(String browser) {
        if (driver == null) {
            String driverPath=testUtils.getDriverPath(browser);
            configProperties = new Properties();
            loadFile("Config.properties", configProperties);
            ORProperties = new Properties();
            loadFile("ObjectRepository.properties", ORProperties);
            switch (browser.toLowerCase()) {
                case "firefox":
                    System.setProperty("webdriver.gecko.driver", driverPath);
                    DesiredCapabilities dc = new DesiredCapabilities();
                    dc.setCapability("marionatte", false);
                    FirefoxOptions opt = new FirefoxOptions();
                    opt.merge(dc);
                    driver = new FirefoxDriver();
                    log.info("Firefox driver configured");
                    break;
                case "ie":
                    System.setProperty("webdriver.ie.driver", driverPath);
                    driver = new InternetExplorerDriver();
                    log.info("Internet Explorer driver configured");
                    break;
                default:
                    System.setProperty("webdriver.chrome.driver", driverPath);
                    driver = new ChromeDriver();
                    log.info("Chrome driver configured");
                    break;
            }
            driver.get(configProperties.getProperty("testsiteurl"));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(configProperties.getProperty("implicit.wait")), TimeUnit.SECONDS);
            wait = new WebDriverWait(driver,5);
            log.info("Driver successfully created");
        }
    }

    @AfterSuite
    public void tearDown() {
        if (driver!=null){
            driver.quit();
        }
    }

    private void loadFile(String fileName, Properties properties) {
        try {
            path = new String[]{"src","test","resources","properties", fileName};
            String var = testUtils.generateFullPath(path);
            fis = new FileInputStream(var);
            properties.load(fis);
        } catch (FileNotFoundException ex) {
             log.error("Error loading properties file: File not Found \n"+ ex.getMessage());
        } catch (IOException e) {
            log.error("Error loading properties file: Input/Output Exception \n"+e.getMessage());
        }
    }

    public void click(String locator){
        driver.findElement(getSelectorType(locator)).click();
        ExtentTestManager.getTest().log(LogStatus.INFO, "[STEP] Clicking on: "+locator);
    }

    public void type(String locator, String value){
        driver.findElement(getSelectorType(locator)).sendKeys(value);
        ExtentTestManager.getTest().log(LogStatus.INFO, "[STEP] Typing in: "+locator+". Entering Value: "+value);
    }

    public void selectFromDropdown(String locator, String visibleText){
        Select select = new Select(driver.findElement(getSelectorType(locator)));
        select.selectByVisibleText(visibleText);
        ExtentTestManager.getTest().log(LogStatus.INFO, "[STEP] Select from: "+locator+" the following value: "+visibleText);
    }

    protected By getSelectorType(String locator){
        switch (locator.toLowerCase()){
            case "css": return By.cssSelector(ORProperties.getProperty(locator));
            case "id": return By.id(ORProperties.getProperty(locator));
            case "classname": return By.className(ORProperties.getProperty(locator));
            case "linktext": return By.linkText(ORProperties.getProperty(locator));
            case "name": return By.name(ORProperties.getProperty(locator));
            default: return By.xpath(ORProperties.getProperty(locator));
        }

    }
}