package base;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Constants;

public class BaseTest {

    public static WebDriver driver;
    public ExtentSparkReporter sparkReporter;
    protected static ExtentReports extent;
    private ExtentTest test;
    public static ThreadLocal<ExtentTest> testLogger = new ThreadLocal<>();
    private String reportFilePath;
    private static final Logger log = LogManager.getLogger(BaseTest.class);
    
    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    // BrowserStack credentials
    private static final String USERNAME = System.getenv("brushabhanujena_6Zhxj1");  
    private static final String AUTOMATE_KEY = System.getenv("kyYqxJMqqih1qWEqtfob");
    private static final String BROWSERSTACK_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeClass
    @Parameters({"browser", "resolution", "runOn"})
    public void setupDriver(@Optional("chrome") String browser, @Optional("1920x1080") String resolution, @Optional("local") String runOn) throws InterruptedException, MalformedURLException, URISyntaxException {
        log.info("Initializing WebDriver...");
        
        if (runOn.equalsIgnoreCase("browserstack")) {
            log.info("Running tests on BrowserStack...");

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browser", browser);
            caps.setCapability("browserstack.resolution", resolution);
            caps.setCapability("browserstack.debug", "true"); 
            caps.setCapability("browserstack.console", "info");

            URL browserStackUrl = new URI(BROWSERSTACK_URL).toURL();
            driver = new RemoteWebDriver(browserStackUrl, caps);
        } else {
            log.info("Running tests locally on: " + browser);
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        }

        driver.manage().window().maximize();
        driver.get(Constants.url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        Thread.sleep(3000);
        log.info("WebDriver initialized successfully.");

        log.info("Initializing Extent Report setup...");
        setupExtentReports();
    }

    private void setupExtentReports() {
        String reportFolder = System.getProperty("user.dir") + "\\reports\\" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        File reportDir = new File(reportFolder);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        reportFilePath = reportFolder + File.separator + "HighLineWarren_Sanity_TestReport" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now()) + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
        sparkReporter.viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.DEVICE})
                .apply();
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        sparkReporter.config().setTheme(Theme.DARK);
        extent.setSystemInfo("Environment", "HighLineWarrenTest");
        extent.setSystemInfo("HostName", "Brushabhanu.jena@intelegencia.com");
        extent.setSystemInfo("UserName", "Brushabhanu Jena");
        sparkReporter.config().setReportName("Automation Test Reports");
        log.info("Extent Report setup completed.");
    }

    @BeforeMethod
    public void beforeMethod(Method testMethod) {
        log.info("Starting test: " + testMethod.getName());
        ExtentTest logger = extent.createTest(testMethod.getName());
        testLogger.set(logger);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        ExtentTest logger = testLogger.get();
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Failed", ExtentColor.RED));
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable().toString(), ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Skipped", ExtentColor.ORANGE));
        } else {
            logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Passed", ExtentColor.GREEN));
        }
    }

    @AfterClass
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
        log.info("Test execution completed. Report generated at: " + reportFilePath);
    }
}

//For headless run of test cases
//public void setupDriver(String browser) throws InterruptedException {
//    log.info("Initializing WebDriver for: " + browser);//newly added
//    Thread.sleep(5000);
//    if (browser.equalsIgnoreCase("chrome")) {
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        driver = new ChromeDriver(options);
//    } else if (browser.equalsIgnoreCase("firefox")) {
//        WebDriverManager.firefoxdriver().setup();
//        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("--headless");
//        driver = new FirefoxDriver(options);
//    } else if (browser.equalsIgnoreCase("edge")) {
//        WebDriverManager.edgedriver().setup();
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        driver = new EdgeDriver(options);
//    }
//    log.info(browser + " WebDriver initialized successfully.");//newly added
//}
//}




