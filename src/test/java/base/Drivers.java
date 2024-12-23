package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ReadProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public enum Drivers {
    CHROME {
        @Override
        public WebDriver getDriver() {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            chromeOptions.addArguments("--lang=tr",
                    "--disable-popup-blocking",
                    "--disable-blink-features=AutomationControlled",
                    "--disable-gpu",
                    "--no-sandbox",
                    "--disable-infobars",
                    "--ignore-certificate-errors",
                    "--disable-translate",
                    "--disable-extensions",
                    "--disable-notifications",
                    "--remote-allow-origins=*");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs);
            return new ChromeDriver(chromeOptions);
        }
    },
    ANDROID {
        @Override
        public WebDriver getDriver() {
            throw new UnsupportedOperationException("Use getNativeDriver() for Android");
        }

        @Override
        public AppiumDriver getNativeDriver() {
            ResourceBundle AppConfiguration = ReadProperties.readProp("AppConfiguration.properties");
            String URL = AppConfiguration.getString("URL");
            String appPackage = AppConfiguration.getString("appPackage");
            String appActivity = AppConfiguration.getString("appActivity");
            String androidUDID = AppConfiguration.getString("androidUDID");
            UiAutomator2Options options = new UiAutomator2Options();
            options
                    .setPlatformName("Android")
                    .setAppPackage(appPackage)
                    .setAppActivity(appActivity)
                    .setNoReset(false)
                    .setDeviceName(androidUDID)
                    .setNewCommandTimeout(Duration.ofSeconds(60000))
                    .eventTimings()
                    .setCapability("disableWindowAnimation", true);
            options.setCapability("autoAcceptAlerts", true);
            options.setCapability("unicodeKeyboard", false);
            options.setCapability("autoAcceptAlerts", true);
            options.setCapability("resetKeyboard", false);

            try {
                return new AppiumDriver(new URL(URL), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Failed to initialize Android driver", e);
            }
        }
    };

    public abstract WebDriver getDriver();

    public AppiumDriver getNativeDriver() {
        throw new UnsupportedOperationException("This driver does not support native Android capabilities.");
    }

    public static Drivers getDriverType(String driverName) {
        for (Drivers driver : Drivers.values()) {
            if (driver.toString().equalsIgnoreCase(driverName)) {
                return driver;
            }
        }
        throw new IllegalArgumentException("Invalid driver type provided: " + driverName);
    }
}