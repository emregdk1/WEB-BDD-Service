package base;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static WebDriver driver;
    protected static Object nativeDriver;
    public static Actions actions;
    private static final String JAVA_VERSION = System.getProperty("java.version");
    private static final String PLATFORM = System.getProperty("os.name").toLowerCase();

    @Given("Setup Driver \"(chrome|firefox|android)\"$")
    public void setUp(String driverType) {
        logger.info("************************************  BeforeScenario  ************************************");
        logger.info("Test environment: Platform: {}, Driver: {}", PLATFORM, driverType);
        logger.info("Java version: {}", JAVA_VERSION);
        try {
            if (driverType.equalsIgnoreCase("android")) {
                nativeDriver = Drivers.getDriverType(driverType).getNativeDriver();
            } else {
                driver = Drivers.getDriverType(driverType).getDriver();
                if (driver != null) {
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
                    driver.manage().window().maximize();
                    actions = new Actions(driver);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Failed to initialize the driver. Unsupported type: {}", driverType, e);
            throw e;
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed.");
        }
        if (nativeDriver != null && nativeDriver instanceof WebDriver) {
            ((WebDriver) nativeDriver).quit();
            logger.info("Native Android driver closed.");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static Object getNativeDriver() {
        return nativeDriver;
    }
}