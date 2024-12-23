package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver getDriver(String driverName)  {

        switch (driverName) {
            case "IE":
                //WebDriverManager.iedriver().setup();



                return new InternetExplorerDriver();
            case "FIREFOX":
                //WebDriverManager.firefoxdriver().setup();



                return new FirefoxDriver();
            case "CHROME":
                //WebDriverManager.chromedriver().setup();
	            ChromeOptions options = new ChromeOptions();
                options.addArguments("" +
                                "--disable-popup-blocking",
                        "--disable-blink-features=AutomationControlled",
                        "--disable-gpu",
                        "--no-sandbox",
                        "disable-infobars",
                        "disable-plugins",
                        "ignore-certificate-errors",
                        "disable-translate",
                        "disable-extensions",
                        "start-maximized",
                        "--disable-notifications",
                        "--remote-allow-origins=*");
	            return new ChromeDriver(options);
            default:
                System.out.print("tarayıcı secilmedi.");
                return  null;
        }
    }
}
